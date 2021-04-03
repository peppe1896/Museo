package museo;

import opera.Opera;
import personale.Amministratore;
import visitatore.Visitatore;

import java.util.*;

public class Museo extends Observable {
    private int costoBiglietto = 10;
    private int bilancio;

    private List<Visitatore> listaVisitatori = new ArrayList<>();
    private List<Biglietto> bigliettiVenduti = new ArrayList<>();
    private Set<Opera> catalogoOpere = new LinkedHashSet<>(); //TODO: chi crea il museo dovrà fornire il catalogo
    private List<Suggerimento> suggerimenti = new ArrayList<>();

    public Museo(){
        catalogoOpere.add(new Opera("Gioconda", "Leonardo Da Vinci", this));
        catalogoOpere.add(new Opera("Notte Stellata", "V. Van Gogh", this));
        catalogoOpere.add(new Opera("Giocasdonda", "Leonardsadso Da Vinci", this));
        catalogoOpere.add(new Opera("Gisadasoconda", "Leonardo asdDa Vinci", this));
    }

    /**
     * Il catalogo opere è immutabile.
     * @return il catalogo totale delle opere.
     */
    public Set<Opera> getCatalogoOpere(){
        return catalogoOpere;
    }
    /**
     * Vende il biglietto per l'ingresso al museo (non alla mostra)
     * @param visitatore è il visitatore che sta comprando il biglietto. Viene salvato nella listaVisitatori
     * @return true se il visitatore riesce a pagare il biglietto
     */
    public boolean vendiBigliettoMuseo(Visitatore visitatore) {
        boolean returns = false;
        if(visitatore.paga(costoBiglietto))
            returns = true;

        listaVisitatori.add(visitatore);
        return returns;
        //TODO: implementare richiesta di registrazione se nuovo utente
    }

    public void registraSuggerimento(Suggerimento suggerimento){
        suggerimenti.add(suggerimento);
        //TODO: potrebbe essere utile observer per notificare che è stato lasciato un
        // suggerimento a l'amministratore
        setChanged();
        notifyObservers();
    }

    public List<Suggerimento> getSuggerimenti(Object richiedente){
        if(richiedente instanceof Amministratore)
            System.out.println("Sono un amministratore");
        else
            System.out.println("Non sono un amministratore");
        return this.suggerimenti;
        //TODO: ancora da implementare
    }
}
