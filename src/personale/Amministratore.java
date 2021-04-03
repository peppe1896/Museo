package personale;

import museo.Museo;
import museo.Suggerimento;
import opera.Opera;

import java.util.LinkedHashMap;
import java.util.Observable;
import java.util.Observer;

public class Amministratore implements Observer {
    private Museo museo;
    private LinkedHashMap<Opera, Integer> suggerimentiPerOpera = new LinkedHashMap<>();
    private int balanceMuseo = 0;

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
    }
    /**
     * Quando arrivano suggerimenti, riceve notifica e li legge.
     * Ho preferito un observer di tipo push: quando mi arriva un suggerimento, aggiungo +1 all'opera
     * corrispondente.
     * //TODO: in questo momento a ogni notifica si prende il suggerimento
     * //TODO: magari si può inserire un counter per vedere in quanti hanno richiesto una certa opera
     * Questo meccanismo si può registrare usando una mappa <Opera,Integer> inserendo il numero di richieste così.
     *
     * Il bilancio è aggiornato in modo pull usando il metodo getBalance del museo.
     * @param o è sempre il Museo
     * @param arg è il suggerimento appena inserito.
     */
    @Override
    public void update(Observable o, Object arg) {
        Opera opera = ((Suggerimento) arg).getSuggerimento();
        int prevInt = suggerimentiPerOpera.get(opera);
        suggerimentiPerOpera.put(opera, ++prevInt);
        balanceMuseo = museo.getBalance(this);
    }

    public LinkedHashMap<Opera, Integer> getSuggerimentiPerOpera(){
        return suggerimentiPerOpera;
    }

    //TODO: il metodo leggi idee è facile: i suggerimenti sono fatti, hai un numero di suggerimenti per
    // ogni opera che esiste nel sistema.

    // Una buona politica di suggerimenti potrebbe essere quella di prendere opere esterne al museo
    // nel caso in cui ci siano un certo numero di suggerimenti, oppure di prendere
}
