package personale;

import museo.Museo;
import museo.Suggerimento;
import opera.Opera;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Observable;
import java.util.Observer;

public class Amministratore implements Observer {
    private Museo museo;
    private LinkedHashMap<Opera, Integer> suggerimentiPerOpera = new LinkedHashMap<>();
    private int balanceMuseo = 0;
    private Strategy strategy;

    private ArrayList<IncaricoMostra> incarichiCreati = new ArrayList<>();

    /**
     * Il costruttore dell'Amministratore costruisce la LinkedHashMap prendendolo dal Catalogo delle opere
     * condiviso tra tutti i Musei.
     * @param museo serve per impostare il museo che deve amministrare.
     */
    public Amministratore(Museo museo){
        this.museo = museo;
        // Inizializzo con 0 suggerimenti per ogni opera.
        for(Opera o: museo.getCatalogoOpere())
            suggerimentiPerOpera.put(o,0);
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
     * @param o è sempre il Museo
     * @param arg è il suggerimento appena inserito.
     */
    @Override
    public void update(Observable o, Object arg) {
        if(arg!=null) {
            Opera opera = ((Suggerimento) arg).getSuggerimento();
            int prevInt = suggerimentiPerOpera.get(opera);
            suggerimentiPerOpera.put(opera, ++prevInt);
        }
        balanceMuseo = museo.getBilancio(this);

        setStrategy();
    }

    /**
     * Questo è il metodo che imposta la strategy a seconda dei parametri. Viene chiamato nel metodo update.
     */
    private void setStrategy(){
        if(balanceMuseo <= 50)
            strategy = new ZeroBudgetStrategy();
        if(balanceMuseo <= 150 && balanceMuseo > 50)
            strategy = new LowBudgetStrategy();
        if(balanceMuseo > 150){
            strategy = new HighBudgetStrategy();
        }
    }

    public LinkedHashMap<Opera, Integer> getSuggerimentiPerOpera(){
        return suggerimentiPerOpera;
    }

    //TODO: il metodo leggi idee è facile: i suggerimenti sono fatti, hai un numero di suggerimenti per
    // ogni opera che esiste nel sistema.

    /**
     * Crea un IncaricoMostra usando una Strategy.
     * // TODO: deve essere possibile creare IncaricoMostra anche in modo personalizzato e fuori dalle strategie.
     * @return Un IncaricoMostra ai soli fini di test.
     */
    public IncaricoMostra createIncaricoMostra(){
        IncaricoMostra incaricoMostra = strategy.createIncaricoMostra(museo);
        if(incaricoMostra != null) {
            Organizzatore organizzatore = (Organizzatore) museo.getOrganizzatori(true).get(0);
            museo.setBilancio(this, museo.getBilancio(this)-incaricoMostra.getBilancio());
            incaricoMostra.setOrganizzatore(organizzatore);
            incarichiCreati.add(incaricoMostra);
        }
        return incaricoMostra;
    }

    public Strategy getStrategy(){
        return strategy;
    }
}
