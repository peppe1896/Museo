package personale.pkgIncaricoMostra;

import museo.Museo;
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
    private int balanceMuseo = 0;
    private Strategy strategy;
    private boolean killStrategy = false;

    private Set<IncaricoMostra> incarichiCreati = new HashSet<>();
    private Set<Opera> accumuloRichieste = new LinkedHashSet<>();

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
     * Quando arrivano suggerimenti, riceve notifica e li legge.
     * Ho preferito un observer di tipo push: quando mi arriva un suggerimento, aggiungo +1 all'opera
     * corrispondente.
     * //TODO: in questo momento a ogni notifica si prende il suggerimento
     * //TODO: magari si può inserire un counter per vedere in quanti hanno richiesto una certa opera
     * Questo meccanismo si può registrare usando una mappa <Opera,Integer> inserendo il numero di richieste così.
     *<p>
     * Il bilancio è aggiornato in modo pull usando il metodo getBalance del museo.
     * <p> Verifico che sia il museo l'Observable. Essendo allo stesso tempo
     * @param o è sempre il Museo
     * @param arg è il suggerimento appena inserito.
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg != null) {
            Opera opera = ((Suggerimento) arg).getSuggerimento();
            int prevInt = suggerimentiPerOpera.get(opera);
            suggerimentiPerOpera.put(opera, ++prevInt);
        }
        balanceMuseo = museo.getBilancio(this);
        checkNumSuggerimenti();
        setStrategy();
        // TODO fare la killStrategy quando ci sono le sale occupate.

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
        if (killStrategy)
            strategy = new KillMostreStrategy(incarichiCreati, this);
        else {
            if(accumuloRichieste.size() >= 2 && balanceMuseo >= 400) {
                strategy = new OnSuggestStrategy(accumuloRichieste);
                // killStrategy = true;
            } else {
                if (balanceMuseo <= 50)
                    strategy = new ZeroBudgetStrategy();
                if (balanceMuseo <= 150 && balanceMuseo > 50)
                    strategy = new LowBudgetStrategy();
                if (balanceMuseo > 150 && balanceMuseo < 5000)
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
     * dal budgetMostre del Museo.
     * // TODO: deve essere possibile creare IncaricoMostra anche in modo personalizzato fuori dalle strategie.
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
