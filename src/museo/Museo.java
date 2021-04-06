package museo;

import museo.sale.Sala;
import museo.sale.SalaFisica;
import museo.sale.SalaVirtuale;
import museo.ticket.Biglietto;
import museo.ticket.TicketMostraFisica;
import museo.ticket.TicketMostraFisicaEVirtuale;
import opera.GestoreOpere;
import opera.Opera;
import personale.*;
import personale.pkgIncaricoMostra.Amministratore;
import visitatore.Acquirente;
import visitatore.Visitatore;
import visitatore.VisitatoreReg;

import java.nio.charset.Charset;
import java.util.*;

public class Museo extends Observable {
    private int costoBiglietto = 10;
    private int bilancio;

    private Map<String, VisitatoreReg> utentiRegistrati = new LinkedHashMap<>();
    private Map<String, ArrayList<TicketMuseo>> ticketMuseoVenduti;
    private Set<Opera> catalogoOpere;
    private List<Suggerimento> suggerimenti = new ArrayList<>();
    private List<Personale> personale = new ArrayList<>();
    private GestoreOpere gestoreOpere;
    private List<Sala> sale = new ArrayList<>();
    private Set<Mostra> mostre = new LinkedHashSet<>();

    private double loadFactorSale = 0.f;
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
        personale.add(new Impiegato());
        personale.add(new Impiegato());
        personale.add(new Impiegato());
        personale.add(new Impiegato());
        personale.add(new Impiegato());
        personale.add(new Impiegato());
        personale.add(new Impiegato());

        personale.add(new Organizzatore(this));
        personale.add(new Organizzatore(this));
        personale.add(new Organizzatore(this));
        personale.add(new Organizzatore(this));

        ticketMuseoVenduti = Map.of(
                "Biglietto Base", new ArrayList<TicketMuseo>(),
                "Ticket Fisica", new ArrayList<TicketMuseo>(),
                "Ticket Fisica-Virtuale", new ArrayList<TicketMuseo>()
        );

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
                new SalaFisica(5),
                new SalaVirtuale(15)
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
            TicketMuseo tmp = new Biglietto(visitatore);
            ticketMuseoVenduti.get("Biglietto Base").add(tmp);
            visitatore.addTicket(tmp);
            returns = true;
        }
        registerVisitor(visitatore);
        return returns;
    }

    private boolean askForRegistration(){
        if(75 > Math.random() * 100)
            return true;
        return false;
    }

    private void registerVisitor(Visitatore visitatore){
        if(askForRegistration()) {
            Visitatore v = new VisitatoreReg(visitatore.getBilancio());
            byte[] array = new byte[12];
            new Random().nextBytes(array);
            String randomString = new String(array, Charset.forName("ISO-8859-1"));
            ((VisitatoreReg)v).setUsername(randomString);
        }
    }

    /**
     * Aggiunge alle Liste di Ticket il Ticket venduto.
     * Mette il valore in Map<Ticket,List<Ticket>>, quindi mette il biglietto nella List mappata da tipo di Ticket.
     * @param visitatore Il visitatore che sta comprando il biglietto.
     * @param mostra
     * @param postoVirtuale Indica se il visitatore vuole un posto virtuale.
     * @return
     */
    public boolean vendiTicketMostraVirtuale(VisitatoreReg visitatore, Mostra mostra, boolean postoVirtuale) {
        if (visitatore.paga(mostra.getCostoBiglietto())) { // il visitatore ha i soldi?
            if (mostra.pagaIngresso(visitatore, postoVirtuale)) { // c'è il posto che il visitatore vuole? (virtuale o fisico)
                if (mostra.isVirtual()) {
                    TicketMuseo tmp = new TicketMostraFisicaEVirtuale(visitatore);
                    ticketMuseoVenduti.get("Ticket Fisica-Virtuale").add(tmp);
                    visitatore.addTicket(tmp);
                    return true;
                }
            }
        }return false;
    }

    public boolean vendiTicketMostraFisica(Visitatore visitatore, Mostra mostra){
        if(visitatore.paga(mostra.getCostoBiglietto())){
            if(mostra.pagaIngresso(visitatore, false)){
                TicketMuseo tmp = new TicketMostraFisica(visitatore);
                ticketMuseoVenduti.get("Ticket Fisica").add(tmp);
                visitatore.addTicket(tmp);
                return true;
            }
        }
        return false;
    }

    public void registraSuggerimento(Suggerimento suggerimento){
        suggerimenti.add(suggerimento);
        setChanged();
        notifyObservers(new StatoMuseo(suggerimento, bilancio, loadFactorSale, null, false));
    }

    /**
     * Metodo usato dal Test Suggerimenti
     * @param richiedente Il test
     * @return I suggerimenti
     */
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
            notifyObservers(new StatoMuseo(null, bilancio, loadFactorSale, null, true));
        }
    }

    public void addBilancio(Object richiedente, int incasso) throws Exception{
        if(richiedente instanceof Organizzatore || richiedente instanceof Amministratore) {
            this.bilancio += incasso;
            setChanged();
            notifyObservers(new StatoMuseo(null, bilancio, loadFactorSale, null, false));
        }
        throw new Exception("Solo un Amministratore o un Organizzatore può aggiungere soldi al Bilancio del Museo");
    }

    public void prelevaBilancio(Object richiedente, int prelievo){
        if(richiedente instanceof Amministratore) {
            this.bilancio -= prelievo;
            setChanged();
            notifyObservers(new StatoMuseo(null, bilancio, loadFactorSale, null, false));
        }
    }

    /** Ottieni una lista degli organizzatori / impiegati / Sale.
     * @param onlyFree Se true, ritorna solo * che sono liberi.
     * @return List di Organizzatori | Impiegati | Sale
     */
    public List<Personale> getOrganizzatori(boolean onlyFree){
        ArrayList<Personale> organizzatori = new ArrayList<>();
        if(onlyFree) {
            for (Personale p : personale)
                if (p instanceof Organizzatore)
                    if (!p.isBusy())
                        organizzatori.add(p);
        }
        else {
            for (Personale p : personale)
                if (p instanceof Organizzatore)
                    organizzatori.add(p);
        }
        return organizzatori;
    }

    public List<Personale> getImpiegati(boolean onlyFree){
        ArrayList<Personale> organizzatori = new ArrayList<>();
        if(onlyFree) {
            for (Personale p : personale)
                if (p instanceof Impiegato)
                    if (!p.isBusy())
                        organizzatori.add(p);
        }
        else {
            for (Personale p : personale)
                if (p instanceof Impiegato)
                    organizzatori.add(p);
        }
        return organizzatori;
    }

    /**
     * Fornisce una Lista di Sale libere che contiene solamente SaleFisiche o SaleFisiche e SaleVirtuali.
     * @param ancheVirtuali se true: tra le Sale prende anche quelle SaleVirtuali | se false, prende solo
     *                      SaleFisiche.
     * @return Lista di Sale.
     */
    public List<Sala> getSale(boolean ancheVirtuali){
        List<Sala> sale = new ArrayList<>();
        if(!ancheVirtuali) {
            for (Sala sala : this.sale)
                if (sala instanceof SalaFisica)
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

    /**
     * Aggiunge una Mostra creata da un Organizzatore. Dice all'Amministratore che può creare un'altra Mostra.
     * @param mostra
     */
    public void addMostra(Mostra mostra){
        this.mostre.add(mostra);
        updateLoadFactorSale();
        setChanged();
        notifyObservers(new StatoMuseo(null, bilancio, loadFactorSale, null, true));
    }

    public Set<Mostra> getMostre(){
        return mostre;
    }

    private void updateLoadFactorSale(){
        int saleOccupate = 0;
        for(Sala sala:sale)
            if(sala.isBusy())
                saleOccupate++;
        loadFactorSale = ((double) saleOccupate)/((double) sale.size());
    }

    /**
     * Viene chiamato dal metodo Organizzatore::chiudiMostra() che ha organizzato la sua Mostra.
     * Toglie la Mostra dalla lista delle Mostre acquistabili. La Mostra, che è impostata come terminata.
     * Manda all'Amministratore in ascolto la Mostra che è appena stata chiusa, così che lui la possa salvare
     * tra le Mostre che sono state chiuse
     * @param organizzatore Colui che ha organizzato una Mostra
     * @param mostra La Mostra
     */
    public void chiudiMostra(Organizzatore organizzatore, Mostra mostra){
        if(organizzatore == mostra.getOrganizzatore()){
            StatoMuseo sm = new StatoMuseo(null, bilancio, loadFactorSale, mostra, false);
            this.mostre.remove(mostra);
            setChanged();
            notifyObservers(sm);
        }
    }
}
