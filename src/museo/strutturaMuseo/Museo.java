package museo.strutturaMuseo;

import museo.sale.Sala;
import museo.sale.SalaFisica;
import museo.sale.SalaVirtuale;
import museo.ticket.Biglietto;
import museo.ticket.TicketMostraFisicaEVirtuale;
import museo.ticket.TicketMuseo;
import opera.GestoreOpere;
import opera.Opera;
import museo.personaleMuseo.*;
import museo.personaleMuseo.amministratore.Amministratore;
import museo.personaleMuseo.amministratore.Strategy;
import museo.personaleMuseo.personaleEsecutivo.Impiegato;
import museo.personaleMuseo.personaleEsecutivo.Organizzatore;
import museo.personaleMuseo.personaleEsecutivo.Personale;
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
    private List<Sala> sale;
    private Set<Mostra> mostre = new LinkedHashSet<>();

    private double loadFactorSale = 0.f;
    /**
     * <h2>Costruttori</h2>
     */

    /**
     * Questo costruttore di Base costruisce creando un Set immutabile di Sale di diverso tipo.
     * Più Musei condividono lo stesso catalogo di Opere, così che possano noleggiarsele tra di loro.
     */
    public Museo(){
        gestoreOpere = new GestoreOpere(this);
        catalogoOpere = gestoreOpere.catalogoOpere;

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
                new SalaVirtuale(15),
                new SalaFisica(5),
                new SalaVirtuale(15)
        );
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
        try{
            visitatore.paga(costoBiglietto);

        }catch (NoMoneyException e) {
            System.err.println(e.getMessage());
            return false;
        }
        TicketMuseo tmp = new Biglietto(visitatore);
        ticketMuseoVenduti.get("Biglietto Base").add(tmp);
        visitatore.addTicket(tmp);
        registerVisitor(visitatore, true);
        return true;

    }

    private boolean askForRegistration(){
        if(75 > Math.random() * 100)
            return true;
        return false;
    }

    /**
     * E' chiamato in forward dal metodo registraVisitatore, che provvede a registrare un utente
     * su richiesta espressa di un utente ester (ad esempio un Test)
     * Registra i Visitatori con una certa probabilità se ho registrazione automatica
     * @param visitatore Visitatore da registrare
     * @param registrazioneAutomatica Se si sta effettuando una registrazione automatica
     */
    private void registerVisitor(Acquirente visitatore, boolean registrazioneAutomatica){
        if(registrazioneAutomatica && askForRegistration()){
                Visitatore vreg = new VisitatoreReg(visitatore.getBilancio());
                byte[] array = new byte[12];
                new Random().nextBytes(array);
                String randomString = new String(array, Charset.forName("ISO-8859-1"));
                ((VisitatoreReg)vreg).setUsername(randomString);
                utentiRegistrati.put(randomString, (VisitatoreReg)vreg);
        }else{
            Visitatore vreg = new VisitatoreReg(visitatore.getBilancio());
            byte[] array = new byte[12];
            new Random().nextBytes(array);
            String randomString = new String(array, Charset.forName("ISO-8859-1"));
            ((VisitatoreReg)vreg).setUsername(randomString);
            utentiRegistrati.put(randomString, (VisitatoreReg)vreg);
        }
    }

    /**
     * Registra un visitatore in modo manuale. Per farlo in modo automatico è presente
     * la funzione registerVisitor, che è privata, e che fa la registrazione di un Visitatore del Museo con probabilità
     * del 60%
     *
     * @param visitatore
     */
    public void registraVisitatore(Acquirente visitatore){
        registerVisitor(visitatore, false);
    }
    /**
     * Aggiunge alle Liste di Ticket il Ticket venduto.
     * Mette il valore in Map<Ticket,List<Ticket>>, quindi mette il biglietto nella List mappata da tipo di Ticket.
     * Aggiunge il ticket venduto anche al visitatore.
     * @param visitatore Il visitatore che sta comprando il biglietto.
     * @param mostra
     * @return
     */
    public boolean vendiTicketMostraVirtuale(Acquirente visitatore, Mostra mostra, boolean preferiscoPostoVirtuale){
        if(mostra.isVirtual()) {
            try {
                visitatore.paga(mostra.getCostoBiglietto()); // il visitatore ha i soldi?
                if (mostra.getPostiRimasti() > 0) {
                    if (preferiscoPostoVirtuale) {
                        if (mostra.getPostiVirtualiRimasti() > 0) {
                            mostra.togliPostoVirtuale();
                        } else {
                            mostra.togliPostoFisico();
                        }
                    }
                    mostra.incassaBiglietto(visitatore);
                    TicketMuseo tmp = new TicketMostraFisicaEVirtuale(visitatore);
                    ticketMuseoVenduti.get("Ticket Fisica-Virtuale").add(tmp);
                    visitatore.addTicket(tmp);
                    return true;
                } else
                    visitatore.ottieniRimborso(mostra.getCostoBiglietto());
            }catch(NoMoneyException e){return false;}
        } else{
            try {
                visitatore.paga(mostra.getCostoBiglietto());
                if (mostra.getPostiRimasti() > 0) {
                    mostra.togliPostoFisico();
                    mostra.incassaBiglietto(visitatore);
                    TicketMuseo tmp = new TicketMostraFisicaEVirtuale(visitatore);
                    ticketMuseoVenduti.get("Ticket Fisica").add(tmp);
                    visitatore.addTicket(tmp);
                    return true;
                }
                else
                    visitatore.ottieniRimborso(mostra.getCostoBiglietto());
            }catch (NoMoneyException e){return false;}
        }
        return false;
    }

    public boolean vendiTicketMostraFisica(Acquirente visitatore, Mostra mostra){
        return vendiTicketMostraVirtuale(visitatore, mostra, false);
    }

    public void registraSuggerimento(Suggerimento suggerimento){
        suggerimenti.add(suggerimento);
        setChanged();
        notifyObservers(new StatoMuseo(suggerimento, null, null, null, null));
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
            notifyObservers();
        }
    }

    public void addBilancio(Object richiedente, int incasso) throws Exception {
        if (richiedente instanceof Organizzatore || richiedente instanceof Amministratore) {
            this.bilancio += incasso;
            setChanged();
            notifyObservers();
        } else {
            throw new Exception("Solo un Amministratore o un Organizzatore può aggiungere soldi al Bilancio del Museo");
        }
    }

    public void prelevaBilancio(Object richiedente, int prelievo){
        if(richiedente instanceof Amministratore || richiedente instanceof Strategy) {
            if(this.bilancio < prelievo) {
                throw new NoMoneyException(prelievo, bilancio);
            }else{
                this.bilancio -= prelievo;
                // setChanged();
                // notifyObservers(new StatoMuseo(null, bilancio, null, null, null));
            }
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
     * Aggiorna il load factor delle Sale del Museo.
     * @param mostra
     */
    public void addMostra(Mostra mostra){
        this.mostre.add(mostra);
        updateLoadFactorSale();
        setChanged();
        notifyObservers(new StatoMuseo(null, null, loadFactorSale, null, true));
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
            StatoMuseo sm = new StatoMuseo(null, null, loadFactorSale, mostra, true);
            this.bilancio += mostra.svuotaCasse();
            this.mostre.remove(mostra);
            updateLoadFactorSale();
            sm.setLoadFactorSale(loadFactorSale);
            setChanged();
            notifyObservers(sm);
        }
    }

    public Map<String, VisitatoreReg> getUtentiRegistrati(){
        return utentiRegistrati;
    }

    public double getLoadFactorSale(Object requester) {
        if(requester instanceof Amministratore)
            return loadFactorSale;
        return 0.f;
    }
}