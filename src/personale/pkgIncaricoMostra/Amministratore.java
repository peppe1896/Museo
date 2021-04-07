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
    private Strategy killMostreStrategy;
    private Strategy idleStrategy;

    private Set<IncaricoMostra> incarichiCreati = new LinkedHashSet<>();
    private Set<Opera> accumuloRichieste = new LinkedHashSet<>();
    private double loadFactorSale = 0.f;
    private List<Mostra> mostreChiuse = new ArrayList<>();
    private boolean goAutomatico = true;
    private boolean semaphoreForStrategies = true;
    private boolean lockCreationIncaricoMostre = false;
    private int countSuggest = 0;
    private int rotations = 0;

    /**
     * Il costruttore dell'Amministratore costruisce la LinkedHashMap prendendolo dal Catalogo delle opere
     * condiviso tra tutti i Musei.
     *
     * @param museo serve per impostare il museo che deve amministrare.
     */
    public Amministratore(Museo museo) {
        this.museo = museo;
        // Inizializzo con 0 suggerimenti per ogni opera.
        loadSuggerimentiPerOpera(this);
        museo.addObserver(this);
        setStrategy();
        killMostreStrategy = new KillMostreStrategy(incarichiCreati, this);
        idleStrategy = new IdleStrategy();
        strategy = idleStrategy;
        bilancioMuseo = museo.getBilancio(this);
    }

    /**
     * Aggiorna il suo stato con lo StateMuseo che viene passato come parametro.
     * Lo StateMuseo viene creato ogni volta che cambia il loadFactorSale, per avviare la strategia
     * di kill delle Mostre, ogni volta che viene creato un suggerimento e ogni volta che viene aggiornato il bilancio
     * <p>
     * Se ho una strategy KillMostre vuol dire che ho un fattore di carico elevato e quindi voglio attivare la pulizia
     * delle Mostre. Altrimenti se la strategia non è cambiata non creo altre Mostre.
     *
     * @param o   è sempre il Museo
     * @param arg StateMuseo.
     */
    @Override
    public void update(Observable o, Object arg) {
        boolean needAdminOp = false;
        boolean strongRestart = false;

        // UPDATE STATO MUSEO
        if (arg != null) {
            StatoMuseo sm = (StatoMuseo) arg;
            Suggerimento suggerimento = sm.getOperaSuggerita();
            Integer bilancioMuseoState = sm.getBilancioMuseo();
            Double loadFactor = sm.getLoadFactorSale();
            Mostra mostra = sm.getMostraChiusa();
            Boolean museoIsReady = sm.getMuseoIsReady();

            if (suggerimento != null) {
                Opera opera = suggerimento.getSuggerimento();
                int prevInt = suggerimentiPerOpera.get(opera);
                suggerimentiPerOpera.put(opera, ++prevInt);
                countSuggest++;
            }
            if (bilancioMuseoState != null) {
                bilancioMuseo = bilancioMuseoState;
                needAdminOp = false;
            }
            if (loadFactor != null) {
                loadFactorSale = loadFactor;
                needAdminOp = false;
            }
            if (mostra != null) {
                mostreChiuse.add(mostra);
                ((KillMostreStrategy) killMostreStrategy).setReady();
                needAdminOp = false;
            }
            if (museoIsReady != null) {
                lockCreationIncaricoMostre = false;
                needAdminOp = true;
            }
            bilancioMuseo = museo.getBilancio(this);
        } else {
            // L'observer pull forza la richiesta di eseguire l'azione. Inoltre è  chiamato da setBilancio del Museo
            strongRestart = true;
            bilancioMuseo = museo.getBilancio(this);
        }
        // CHECK SUGGERIMENTI
        if (countSuggest > 9) {
            checkNumSuggerimenti();
            countSuggest = 0;
        }
        if(strongRestart){
            updateSemaforo();
            setStrategy();
            azioneAmministratore();
        }
        else {

            // AMMINISTRATORE AUTOMATICO
            updateSemaforo();

            // se ho il semaforo false (cioè Giallo o Rosso), allora calcolo la strategia, e se la
            // strategia è kill verifico che non stia già uccidendo qualche altra mostra.
            if (goAutomatico && needAdminOp) {
                if (!semaphoreForStrategies) {
                    setStrategy();
                    boolean killerIsReady = ((KillMostreStrategy) killMostreStrategy).isReady();
                    if (strategy instanceof KillMostreStrategy && !killerIsReady) {
                        ;
                    } else
                        azioneAmministratore();

                } else {
                    if (!lockCreationIncaricoMostre) {
                        setStrategy();
                        azioneAmministratore();
                    }
                }
            }

            // PULIZIA STRUTTURE DATI
            rotations++;
            if (rotations == 100) {
                removeTrash();
                rotations = 0;
            }
        }
    }

    private void removeTrash(){
        for(IncaricoMostra incaricoMostra : incarichiCreati)
            if(!incaricoMostra.isKillable())
                incarichiCreati.remove(incaricoMostra);
    }

    /**
     * Il semaforo rosso (false) mette in pausa l'amministratore. Con il semaforo rosso, l'Amministratore
     * può solamente killare o stare in idle.
     */
    private void updateSemaforo(){
        int incarichiAttivi = museo.getMostre().size();

        if(incarichiAttivi < 2 && loadFactorSale < 0.65) // semaforo verde
            semaphoreForStrategies = true;
        else
            semaphoreForStrategies = false;
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
        if(!semaphoreForStrategies){ // semaforo ROSSO
            if (loadFactorSale > 0.9)
                strategy = killMostreStrategy;
            else    // semaforo GIALLO
                strategy = idleStrategy;
        } else {    // semaforo VERDE
            // Se il semaforo è verde sono sicuro che ho al massimo 2 mostre attive. Imposto strategia di creazione
            if (accumuloRichieste.size() >= 2 && bilancioMuseo >= 400) {
                strategy = new OnSuggestStrategy(accumuloRichieste, this);
            } else {
                if (bilancioMuseo < 1000)
                    strategy = idleStrategy;
                if (1000 <= bilancioMuseo && bilancioMuseo < 2500)
                    strategy = new LowBudgetStrategy();
                if (2500 <= bilancioMuseo && bilancioMuseo < 5000)
                    strategy = new PersonalStrategy(5, true);
                if (5000 <= bilancioMuseo && bilancioMuseo < 10000)
                    strategy = new PersonalStrategy(7, false);
                else
                    strategy = idleStrategy;
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
     * @return : Ai soli fini di test. Non è necessario prendere il valore di ritorno per avere questo comportamento
     */
    public IncaricoMostra azioneAmministratore(){
        IncaricoMostra incaricoMostra = strategy.strategyMethod(museo);
        if(incaricoMostra != null && !lockCreationIncaricoMostre) {
            lockCreationIncaricoMostre = true;
            Organizzatore organizzatore = (Organizzatore) museo.getOrganizzatori(true).get(0);
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
            strategy = new PersonalStrategy(numeroOpere, ancheVirtuale);
        }else if(numeroStrategia == 3){ // Num 3 = strategia suggerimento
            strategy = new OnSuggestStrategy(accumuloRichieste, this);
        }
        IncaricoMostra incaricoMostra = strategy.strategyMethod(museo);
        if(incaricoMostra != null) {
            Organizzatore organizzatore = (Organizzatore) museo.getOrganizzatori(true).get(0);
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
