package museo;

import museo.sale.SalaFisica;
import museo.sale.SalaVirtuale;
import museo.ticket.Biglietto;
import museo.ticket.TicketMostraFisica;
import museo.ticket.TicketMostraVirtuale;
import opera.GestoreOpere;
import opera.Opera;
import personale.*;
import personale.pkgIncaricoMostra.Amministratore;
import visitatore.Visitatore;

import java.util.*;

public class Museo extends Observable {
    private int costoBiglietto = 10;
    private int bilancio;

    private List<Visitatore> listaVisitatori = new ArrayList<>();
    private LinkedHashMap<String, List<TicketMuseo>> ticketMuseoVenduti;
    private Set<Opera> catalogoOpere;
    private List<Suggerimento> suggerimenti = new ArrayList<>();
    private List<Personale> personale = new ArrayList<>();
    private GestoreOpere gestoreOpere;
    private List<Sala> sale = new ArrayList<>();
    private ArrayList<Mostra> mostre = new ArrayList<>();

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

        ticketMuseoVenduti = new LinkedHashMap<>();
        ticketMuseoVenduti.put("Biglietto Base", new ArrayList<>());
        ticketMuseoVenduti.put("Ticket Mostra", new ArrayList<>());
        ticketMuseoVenduti.put("Ticket Virtuale", new ArrayList<>());

        sale = List.of(new SalaFisica(5),
                new SalaFisica(5),
                new SalaVirtuale(15),
                new SalaFisica(5),
                new SalaVirtuale(15),
                new SalaFisica(5),
                new SalaVirtuale(15),
                new SalaFisica(5),
                new SalaVirtuale(15),
                new SalaFisica(5),
                new SalaVirtuale(15),
                new SalaFisica(5)
        );

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
        if(visitatore.paga(costoBiglietto)) {
            ticketMuseoVenduti.get("Biglietto Base").add(new Biglietto(visitatore));
            returns = true;
        }
        listaVisitatori.add(visitatore);
        return returns;
        //TODO: implementare richiesta di registrazione se nuovo utente
    }

    public boolean vendiTicketMostra(Visitatore visitatore, Mostra mostra){
        if(visitatore.paga(mostra.getCostoBiglietto())){
            if(mostra.isVirtual())
                ticketMuseoVenduti.get("Ticket Virtuale").add(new TicketMostraFisica(visitatore));
            else
                ticketMuseoVenduti.get("Ticket Mostra").add(new TicketMostraFisica(visitatore));
            return true;
        }
        return false;
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
        if(richiedente instanceof Organizzatore || richiedente instanceof Amministratore) {
            this.bilancio += incasso;
            setChanged();
            notifyObservers();
        }
    }

    public void prelevaBilancio(Object richiedente, int prelievo){
        if(richiedente instanceof Amministratore) {
            this.bilancio -= prelievo;
            setChanged();
            notifyObservers();
        }
    }

    /** Ottieni una lista degli organizzatori / impiegati / Sale.
     * @param onlyFree Se true, ritorna solo * che sono liberi.
     * @return List di Organizzatori | Impiegati | Sale
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

    public List<Sala> getSale(boolean onlyVirtual){
        List<Sala> sale = new ArrayList<>();
        if(onlyVirtual) {
            for (Sala sala : this.sale)
                if (sala instanceof SalaVirtuale)
                    if (!sala.isBusy())
                        sale.add(sala);
        }
        else{
            for(Sala sala: this.sale)
                if(!sala.isBusy())
                    sale.add(sala);
        }
        return sale;
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

    public void addMostra(Mostra mostra){
        this.mostre.add(mostra);
    }

    public List<Mostra> getMostre(){
        return mostre;
    }
}
