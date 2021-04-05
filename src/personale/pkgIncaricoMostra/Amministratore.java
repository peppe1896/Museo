package personale.pkgIncaricoMostra;

import museo.Mostra;
import museo.Museo;
import museo.StateMuseo;
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
     * @param o è sempre il Museo
     * @param arg StateMuseo.
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg != null) {
            StateMuseo sm = (StateMuseo) arg;
            Suggerimento suggerimento = sm.getOperaSuggerita();
            Mostra mostra = sm.getMostraChiusa();
            // Aggiungo suggerimenti
            if(suggerimento != null) {
                Opera opera = suggerimento.getSuggerimento();
                int prevInt = suggerimentiPerOpera.get(opera);
                suggerimentiPerOpera.put(opera, ++prevInt);
            }
            // Aggiorno bilancio
            bilancioMuseo = sm.getBilancioMuseo();
            // Aggiorno fattore di carico
            loadFactorSale = sm.getLoadFactorSale();
            // Aggiungo eventuale Mostra chiusa.
            if(mostra != null)
                mostreChiuse.add(mostra);
        }

        checkNumSuggerimenti();
        setStrategy();
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
     * Verifica che non si possa attivare la Strategy OnSuggestStrategy, altrimenti ne segue una per come il museo
     * si può permettere.
     */
    private void setStrategy(){
        if (loadFactorSale > 0.5)
            strategy = new KillMostreStrategy(incarichiCreati, this);
        else {
            if(accumuloRichieste.size() >= 2 && bilancioMuseo >= 400) {
                strategy = new OnSuggestStrategy(accumuloRichieste);
            } else {
                if (bilancioMuseo <= 50)
                    strategy = new ZeroBudgetStrategy();
                if (bilancioMuseo <= 150 && bilancioMuseo > 50)
                    strategy = new LowBudgetStrategy();
                if (bilancioMuseo > 150 && bilancioMuseo < 5000)
                    strategy = new HighBudgetStrategy();

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
     * dal budgetMostre del Museo. || Se invece è impostata come Strategy quella di chiudere le mostre,
     * chiude la mostra secondo quella Strategia. In qualsiasi caso, è lo strategyMethod che sa la procedura.
     * // TODO: ancora non si prevede di poter trovare un Organizzatore occupato.
     * @return : Ai soli fini di test. Non è necessario prendere il valore di ritorno per avere questo comportamento
     */
    public IncaricoMostra createIncaricoMostra(){
        // strategyMethod crea un incarico a seconda della strategia, oppure distrugge una mostra
        IncaricoMostra incaricoMostra = strategy.strategyMethod(museo);
        if(incaricoMostra != null) {
            Organizzatore organizzatore = (Organizzatore) museo.getOrganizzatori(true).get(0);
            museo.prelevaBilancio(this, incaricoMostra.getBilancio());
            incaricoMostra.setOrganizzatore(organizzatore);
            incarichiCreati.add(incaricoMostra);
            organizzatore.setIncaricoAttuale(incaricoMostra);
        }
        return incaricoMostra;
    }

    public Strategy getStrategy(){
        return strategy;
    }

    public Set<IncaricoMostra> getIncarichiCreati(){
        return incarichiCreati;
    }
}
