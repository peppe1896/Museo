package museo;

import opera.GestoreOpere;
import opera.Opera;
import personale.*;
import personale.strategy.Amministratore;
import visitatore.Visitatore;

import java.util.*;

public class Museo extends Observable {
    private int costoBiglietto = 10;
    private int bilancio;

    private List<Visitatore> listaVisitatori = new ArrayList<>();
    private List<Biglietto> bigliettiVenduti = new ArrayList<>();
    private Set<Opera> catalogoOpere; //TODO: chi crea il museo dovrà fornire il catalogo
    private List<Suggerimento> suggerimenti = new ArrayList<>();
    private List<Personale> personale = new ArrayList<>();
    private GestoreOpere gestoreOpere;
    /**
     * <h2>Costruttori</h2>
     */
    public Museo(){
        gestoreOpere = new GestoreOpere(this);
        catalogoOpere = gestoreOpere.getCatalogoOpere();

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
        return catalogoOpere; //Todo: sei sicuro che chi usa questo set non rischa di rovinarlo?
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

    public void addBilancio(Object richiedente, int incasso){
        this.bilancio += incasso;
        setChanged();
        notifyObservers();
    }

    public void prelevaBilancio(Object richiedente, int prelievo){
        this.bilancio -= prelievo;
        setChanged();
        notifyObservers();
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

    public List<Personale> getImpiegati(boolean onlyFree){
        ArrayList<Personale> organizzatori = new ArrayList<>();
        if(onlyFree)
            for(Personale p: personale)
                if(p instanceof Impiegato)
                    if(!p.isBusy())
                        organizzatori.add(p);
        else
            for(Personale pp:personale)
                if(pp instanceof Impiegato)
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
    public GestoreOpere getGestoreOpere(){
        return gestoreOpere;
    }
}
