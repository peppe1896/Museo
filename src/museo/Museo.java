package museo;

import opera.Opera;
import personale.Amministratore;
import personale.Impiegato;
import personale.Organizzatore;
import personale.Personale;
import visitatore.Visitatore;

import java.util.*;

public class Museo extends Observable {
    private int costoBiglietto = 10;
    private int bilancio;

    private List<Visitatore> listaVisitatori = new ArrayList<>();
    private List<Biglietto> bigliettiVenduti = new ArrayList<>();
    private Set<Opera> catalogoOpere = new LinkedHashSet<>(); //TODO: chi crea il museo dovrà fornire il catalogo
    private List<Suggerimento> suggerimenti = new ArrayList<>();
    private List<Personale> personale = new ArrayList<>();
    /**
     * <h2>Costruttori</h2>
     */
    public Museo(){
        catalogoOpere.add(new Opera("La Gioconda", "Leonardo Da Vinci", this, 50));
        catalogoOpere.add(new Opera("Notte Stellata", "V. Van Gogh", this, 50));
        catalogoOpere.add(new Opera("La Persistenza della Memoria", "Salvador Dalì", this, 50));
        catalogoOpere.add(new Opera("L'Urlo", "Edvard Munch", this, 50));
        catalogoOpere.add(new Opera("La Ragazza con l'Orecchino di Perla", "Jean Vermeer", this, 50));
        catalogoOpere.add(new Opera("L'Urlo", "Edvard Munch", this, 50));
        catalogoOpere.add(new Opera("La Nascita di Venere", "Sandro Botticelli", null, 50));
        catalogoOpere.add(new Opera("La Venere di Urbino", "Tiziano", null, 50));
        catalogoOpere.add(new Opera("American Gothic", "Grant Wood", null, 50));
        catalogoOpere.add(new Opera("Olympia", "Edouart Manet", null, 50));
        catalogoOpere.add(new Opera("Ophelia", "John Everett Millais", null, 50));

        personale.add(new Impiegato());
        personale.add(new Impiegato());
        personale.add(new Impiegato());
        personale.add(new Impiegato());
        personale.add(new Impiegato());

        personale.add(new Organizzatore(this));
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
     * Ottieni il bilancio attuale del Museo.
     * @param richiedente è chi richiede. Se è un Amministratore, gli ritorna il bilancio corretto, altrimenti 0.
     * @return bilancio del museo oppure 0.
     */
    public int getBilancio(Object richiedente){
        if(richiedente instanceof Amministratore)
            return bilancio;
        return 0;
    }
    /**
     * Imposta il bilancio del Museo, sovrascrivendo il valore precedente.
     * @param richiedente chi chiama il metodo. Deve essere un amministratore per eseguire questa modifica di bilancio.
     * @param bilancio  è il bilancio che verrà impostato come nuovo bilancio del Museo.
     */
    public void setBilancio(Object richiedente, int bilancio){
        if(richiedente instanceof Amministratore) {
            this.bilancio = bilancio;
            setChanged();
            notifyObservers();
        }
    }

    /** Ottieni una lista degli organizzatori.
     * @param onlyFree Se true, ritorna solo gli organizzatori che sono liberi di potere organizzare qualche mostra.
     * @return tutti gli organizzatori, anche quelli che sono attualmente occupati.
     */
    public List<Personale> getOrganizzatori(boolean onlyFree){
        ArrayList<Personale> organizzatori = new ArrayList<>();
        if(onlyFree)
            for(Personale p: personale)
                if(p instanceof Organizzatore)
                    if(!p.isBusy())
                        organizzatori.add(p);
        else
            for(Personale pp:personale)
                if(pp instanceof Organizzatore)
                    organizzatori.add(p);
        return organizzatori;
    }

    /**
     *
     * @return List di tutte le opere di cui questo museo è il proprietario.
     */
    public List<Opera> getOpereMuseo(){
        ArrayList<Opera> opere = new ArrayList<>();
        for(Opera opera:catalogoOpere)
            if(opera.getProprietario() == this)
                opere.add(opera);
        return opere;
    }
}
