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

    /**
     * <h2>Costruttori</h2>
     */
    public Museo(){
        catalogoOpere.add(new Opera("Gioconda", "Leonardo Da Vinci", this));
        catalogoOpere.add(new Opera("Notte Stellata", "V. Van Gogh", this));
        catalogoOpere.add(new Opera("Giocasdonda", "Leonardsadso Da Vinci", this));
        catalogoOpere.add(new Opera("Gisadasoconda", "Leonardo asdDa Vinci", this));
    }

    public Museo(Set<Opera> catalogoOpere){
        this.catalogoOpere = catalogoOpere;
    }


    /**
     * <h2>Metodi</h2>
     *
     * <p>Il catalogo opere è immutabile.
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
        notifyObservers(suggerimento);
    }

    public List<Suggerimento> getSuggerimenti(Object richiedente){
        if(richiedente instanceof Amministratore)
            System.out.println("Sono un amministratore");
        else
            System.out.println("Non sono un amministratore");
        return this.suggerimenti;
        //TODO: ancora da implementare
    }

    public void addOpera(Opera opera){
        this.catalogoOpere.add(opera);
    }

    public boolean remOpera(Opera opera){
        return this.catalogoOpere.remove(opera);
    }

    /**
     *
     * @param richiedente è chi richiede. Se è un Amministratore, gli ritorna il bilancio corretto, altrimenti 0.
     * @return bilancio del museo oppure 0.
     */
    public int getBalance(Object richiedente){
        if(richiedente instanceof Amministratore)
            return bilancio;
        return 0;
    }
}
