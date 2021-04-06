package personale.pkgIncaricoMostra;

import museo.Mostra;
import museo.Museo;
import museo.StatoMuseo;
import museo.Suggerimento;
import opera.Opera;
import personale.Organizzatore;
import test.creaMostra.TestStrategyIncaricoMostra;

import java.util.*;
/**
 * Amministra un Museo, tenendo d'occhio il numero di suggerimenti per tutte le opere che esistono ed elaborando strategie
 * per creare (o eventualmente non creare) Mostre. Per essere più precisi, crea degli IncarichiMostre, i quali verranno
 * affidati agli Organizzatori, che si occupano della creazione della Mostra. L'Amministratore tiene traccia degli Incarichi
 * conferiti, salvandoli dentro l'ArrayList incarichiCreati.
 */

public class Amministratore implements Observer {
    private Museo museo;
    private LinkedHashMap<Opera, Integer> suggerimentiPerOpera = new LinkedHashMap<>();
    private int bilancioMuseo = 0;
    private Strategy strategy;

    private Set<IncaricoMostra> incarichiCreati = new HashSet<>();
    private Set<Opera> accumuloRichieste = new LinkedHashSet<>();
    private double loadFactorSale = 0.f;
    private List<Mostra> mostreChiuse = new ArrayList<>();
    private boolean goAutomatico = true;
    private boolean semaphore = true;
    private int preventRaceCondition = 3;

    /**
     * Il costruttore dell'Amministratore costruisce la LinkedHashMap prendendolo dal Catalogo delle opere
     * condiviso tra tutti i Musei.
     * @param museo serve per impostare il museo che deve amministrare.
     */
    public Amministratore(Museo museo){
        this.museo = museo;
        // Inizializzo con 0 suggerimenti per ogni opera.
        loadSuggerimentiPerOpera(this);
        museo.addObserver(this);
        setStrategy();
    }
    /**
     * Aggiorna il suo stato con lo StateMuseo che viene passato come parametro.
     * Lo StateMuseo viene creato ogni volta che cambia il loadFactorSale, per avviare la strategia
     * di kill delle Mostre, ogni volta che viene creato un suggerimento e ogni volta che viene aggiornato il bilancio
     *
     * Se ho una strategy KillMostre vuol dire che ho un fattore di carico elevato e quindi voglio attivare la pulizia
     * delle Mostre. Altrimenti se la strategia non è cambiata non creo altre Mostre.
     * @param o è sempre il Museo
     * @param arg StateMuseo.
     */
    @Override
    public void update(Observable o, Object arg) {
        if(arg!=null) {
            StatoMuseo sm = (StatoMuseo) arg;
            Suggerimento suggerimento = sm.getOperaSuggerita();
            Mostra mostra = sm.getMostraChiusa();

            if (suggerimento != null) {
                Opera opera = suggerimento.getSuggerimento();
                int prevInt = suggerimentiPerOpera.get(opera);
                suggerimentiPerOpera.put(opera, ++prevInt);
            }
            bilancioMuseo = sm.getBilancioMuseo();

            loadFactorSale = sm.getLoadFactorSale();

            if (mostra != null)
                mostreChiuse.add(mostra);

            semaphore = sm.getMostraAggiunta();
        }
        checkNumSuggerimenti();
        setStrategy();
        if(strategy instanceof KillMostreStrategy || strategy instanceof NoCreationStrategy)
            azioneAmministratore();
        else
        {
            if(goAutomatico && semaphore) {
                azioneAmministratore();
            }
        }
    }

    /**
     * Resetta la Mappa dei suggerimenti mettendo a 0 il count di tutti i suggerimenti e ricaricando il catalogoOpere.
     * La voglio ricaricare in fase di test, oppure in fase di caricamento tramite il costruttore, e quindi tramite
     * Amministratore.
     */
    public void loadSuggerimentiPerOpera(Object requester){
        if(requester instanceof TestStrategyIncaricoMostra || requester instanceof Amministratore)
        suggerimentiPerOpera = new LinkedHashMap<>();
        for(Opera o: museo.getCatalogoOpere())
            suggerimentiPerOpera.put(o,0);
    }
    /**
     * Imposta la Strategia in base al fattore di carico delle Sale: se ho un fattore alto, voglio che le Mostre vengano
     * annullate forzatamente; se ho un fattore di carico medio-alto, evito di crearne ancora fino a quando non vengono
     * fatte terminare quelle già attive, e infine se ho un fattore di carico medio basso, ne creo guardando il budget
     * del museo.
     */
    private void setStrategy(){
        if (loadFactorSale > 0.9)
            strategy = new KillMostreStrategy(incarichiCreati, this);
        else if (0.5 < loadFactorSale && loadFactorSale <=0.9){
            strategy = new NoCreationStrategy();
        }else {
            if(accumuloRichieste.size() >= 2 && bilancioMuseo >= 1000) {
                strategy = new OnSuggestStrategy(accumuloRichieste, this);
            } else {
                if (bilancioMuseo == 0)
                    strategy = new NoCreationStrategy();
                if (0 < bilancioMuseo && bilancioMuseo <= 1000)
                    strategy = new LowBudgetStrategy();
                if (1000 < bilancioMuseo && bilancioMuseo < 5000)
                    strategy = new PersonalStrategy(5, true);
                if (5000 < bilancioMuseo && bilancioMuseo < 10000)
                    strategy = new PersonalStrategy(7, false);
            }
        }
    }

    /**
     * Controlla il numero dei suggerimenti e aggiunge al set di {@code accumuloRichieste} l'opera suggerita.
     * Questo {@code Set} verrà passato al costruttore di {@code AutomaticStrategy} che metterà il numero più
     * alto possibile di opere suggerite nella prossima mostra organizzata.
     */
    private void checkNumSuggerimenti(){
        for(Map.Entry<Opera,Integer> entry: suggerimentiPerOpera.entrySet()){
            if(entry.getValue()==5){
                accumuloRichieste.add(entry.getKey());
            }
        }
    }

    public LinkedHashMap<Opera, Integer> getSuggerimentiPerOpera(){
        return suggerimentiPerOpera;
    }

    /**
     * Crea un IncaricoMostra usando una Strategy e stanzia il denaro previsto dalla strategia, togliendolo
     * dal budgetMostre del Museo. Tramite le Strategy, chiede in noleggio le Opere
     * d'arte che possono essere noleggiate e che servono per la Mostra
     * || Se invece è impostata come Strategy quella di chiudere le mostre,
     * chiude la mostra secondo quella Strategia. In qualsiasi caso, è lo strategyMethod che sa la procedura.
     * // TODO: ancora non si prevede di poter trovare un Organizzatore occupato.
     * @return : Ai soli fini di test. Non è necessario prendere il valore di ritorno per avere questo comportamento
     */
    public IncaricoMostra azioneAmministratore(){
        IncaricoMostra incaricoMostra = strategy.strategyMethod(museo);
        if(incaricoMostra != null) {
            semaphore = false;
            // Se passo in questo if, ho creato un IncaricoMostra e quindi la organizzo
            Organizzatore organizzatore = (Organizzatore) museo.getOrganizzatori(true).get(0);
            museo.prelevaBilancio(this, incaricoMostra.getBilancio());
            incaricoMostra.setOrganizzatore(organizzatore);
            incarichiCreati.add(incaricoMostra);
            organizzatore.setIncaricoAttuale(incaricoMostra);
            organizzatore.svolgiIncaricoAssegnato();
        }
        return incaricoMostra;
    }

    public void resetSuggerimentiPerOpere(List<Opera> opereDaResettare){
        for(Opera opera:opereDaResettare) {
            suggerimentiPerOpera.replace(opera, 0);
            this.accumuloRichieste.remove(opera);
        }
    }

    public List<Mostra> getMostreChiuse(){
        return mostreChiuse;
    }

    public double getLoadFactorSale(){
        return loadFactorSale;
    }

    /**
     * Metodi test.
     * @param numeroStrategia
     * @return
     */
    public IncaricoMostra forceStrategyExecution(int numeroStrategia){
        if(numeroStrategia == 0) {      // Num 0 = strategia Killer
            strategy = new KillMostreStrategy(incarichiCreati, this);
        }else if(numeroStrategia == 1){ // Num 1 = strategia Lowbudget
            strategy = new LowBudgetStrategy();
        }else if(numeroStrategia == 2){ // Num 2 = strategia Highbudget
            strategy = new PersonalStrategy(12, false);
        }else if(numeroStrategia == 3){ // Num 3 = strategia suggerimento
            strategy = new OnSuggestStrategy(accumuloRichieste, this);
        }
        IncaricoMostra incaricoMostra = strategy.strategyMethod(museo);
        if(incaricoMostra != null) {
            Organizzatore organizzatore = (Organizzatore) museo.getOrganizzatori(true).get(0);
            museo.prelevaBilancio(this, incaricoMostra.getBilancio());
            incaricoMostra.setOrganizzatore(organizzatore);
            incarichiCreati.add(incaricoMostra);
            organizzatore.setIncaricoAttuale(incaricoMostra);
            organizzatore.svolgiIncaricoAssegnato();
        }
        return incaricoMostra;
    }

    /**
     * Questo solo per forzare la PersonalStrategy
     * @param numeroStrategia
     * @param numeroOpere
     * @param ancheVirtuale
     * @return
     */
    public IncaricoMostra forceStrategyExecution(int numeroStrategia, int numeroOpere, boolean ancheVirtuale){
        if(numeroStrategia == 0) {      // Num 0 = strategia Killer
            strategy = new KillMostreStrategy(incarichiCreati, this);
        }else if(numeroStrategia == 1){ // Num 1 = strategia Lowbudget
            strategy = new LowBudgetStrategy();
        }else if(numeroStrategia == 2){ // Num 2 = strategia Highbudget
            strategy = new PersonalStrategy(numeroOpere, false);
        }else if(numeroStrategia == 3){ // Num 3 = strategia suggerimento
            strategy = new OnSuggestStrategy(accumuloRichieste, this);
        }
        IncaricoMostra incaricoMostra = strategy.strategyMethod(museo);
        if(incaricoMostra != null) {
            Organizzatore organizzatore = (Organizzatore) museo.getOrganizzatori(true).get(0);
            museo.prelevaBilancio(this, incaricoMostra.getBilancio());
            incaricoMostra.setOrganizzatore(organizzatore);
            incarichiCreati.add(incaricoMostra);
            organizzatore.setIncaricoAttuale(incaricoMostra);
            organizzatore.svolgiIncaricoAssegnato();
        }
        return incaricoMostra;
    }

    public void setAmministratoreAutomatico(boolean automatico){
        this.goAutomatico = automatico;
    }
}
