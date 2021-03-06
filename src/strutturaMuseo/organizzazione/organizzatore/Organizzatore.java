package strutturaMuseo.organizzazione.organizzatore;

import strutturaMuseo.museoAndAdmin.Museo;
import opera.GestoreOpere;
import opera.Opera;
import strutturaMuseo.museoAndAdmin.IncaricoMostra;
import strutturaMuseo.organizzazione.Impiegato;
import strutturaMuseo.organizzazione.Personale;
import visitatore.Acquirente;

import java.util.*;

/**
 * Organizzatore sviluppa un IncaricoMostra fornito da un amministratore. se fosse richiesto dall'Amministratore,
 * questa mostra deve essere annullata rilasciando tutte le opere, e questa richiesta è fatta usando l'observer.
 * Organizzatore non ha riferimento alla Mostra, ma installa il riferimento alla Mostra che lui crea a IncaricoMostra,
 * e quando qualche metodo di Organizzatore ha bisogno di fare riferimento a Mostra, usa il metodo getMostra()
 * dell'IncaricoMostra.
 */

public class Organizzatore extends Personale implements Observer {
    private Museo museo;
    private IncaricoMostra incaricoAttuale;

    public Organizzatore(Museo museo){
        this.museo = museo;
    }

    public void setIncaricoAttuale(IncaricoMostra incaricoMostra){
        this.setBusy();
        incaricoAttuale = incaricoMostra;
        incaricoAttuale.addObserver(this);
    }

    /**
     * Viene chiamato per organizzare la Mostra. svolgiIncarico dell'IncaricoMostra serve solamente
     * a stampare a schermo che la Mostra è stata organizzata.
     */
    @Override
    public void svolgiIncaricoAssegnato() {
        organizzaMostra();
        incaricoAttuale.svolgiIncarico();
    }

    /**
     * Chiama in forward i metodi delle singole operazioni.
     * Aggiungo la mostra prendendo il riferimento alla mostra dall'Incarico.
     * Questo metodo è chiamato
     */
    private void organizzaMostra(){
        boolean ancheVirtuale = incaricoAttuale.isAncheVirtuale();
        scegliImpiegati(ancheVirtuale);
        assegnaCompiti(ancheVirtuale);
        pagaPersonale();
        try {
            verificaOpere();
        } catch (Exception e){System.err.println(e.getMessage());}
        setMostra(ancheVirtuale);
        svolgiCompitiImpiegati();
        museo.addMostra(incaricoAttuale.getMostra());
    }

    /**
     * Sceglie gli impiegati da far lavorare nella mostra e li imposta come occupati. Imposta questo ArrayList
     * all'IncaricoMostra. Il valore 3 perché prevedo ancora solo 3 Incarichi per i dipendenti
     */
    private void scegliImpiegati(boolean ancheVirtuale){
        int numeroImpiegatiRichiesti;
        if(ancheVirtuale)
            numeroImpiegatiRichiesti = 4;
        else
            numeroImpiegatiRichiesti = 3;
        ArrayList<Personale> impiegati = new ArrayList<>();
        for(Personale p:museo.getImpiegati(true)) {
            if (impiegati.size() < numeroImpiegatiRichiesti) {
                if (!p.isBusy()) {
                    p.setBusy();
                    impiegati.add(p);
                }
            }
        }
        incaricoAttuale.setImpiegati(impiegati);
    }

    /**
     * Affitta le opere pagando il prezzo di noleggio
     */
    private void verificaOpere() throws Exception{
        for(Opera opera:incaricoAttuale.getOpereMostra())
            if(!opera.isBusy())
                if(opera.getAffittuario()!=museo)
                    throw new Exception("Attenzione! " + opera.getNome() + " non è stata affittata");
    }

    /**
     * Pago gli impiegati e l'organizzatore: 10 gli impiegati e 20 l'organizzatore.
     * Può lanciare l'eccezione NoMoneyException
     */
    private void pagaPersonale(){
        try{
            incaricoAttuale.prelevaDenaro(this, 20);
            this.pagami(20);
            for(Personale p:incaricoAttuale.getImpiegati()) {
                incaricoAttuale.prelevaDenaro(this, 10);
                p.pagami(10);
            }
        } catch (Exception e){}
    }

    /**
     * Se è solo fisica non ho bisogno di sito web e di altri incarichi.
     * @param ancheVirtuale
     */
    private void assegnaCompiti(boolean ancheVirtuale) {
        IncaricoImpiegato incarichi[];
        if(ancheVirtuale) {
            incarichi = new IncaricoImpiegato[4];
            incarichi[3] = new OrganizzaSitoWeb();
        }
        else
            incarichi = new IncaricoImpiegato[3];
        incarichi[0] = new CreaPubblicità();
        incarichi[1] = new SpostareOpera();
        incarichi[2] = new PulizieSalaFisica();
        int index = 0;
        for(Personale p: incaricoAttuale.getImpiegati()){
            try {
                p.setIncaricoImpiegato(incarichi[index]);
                ((ImpostaIncarico)incarichi[index]).setIncaricoTo((Impiegato)p);
                index++;
            }catch (Exception e){}
        }
    }

    /**
     * Creo la mostra e gli attacco l'Organizzatore come observer. Dopodiché prelevo le rimanenze
     * tra i fondi stanziati per l'IncaricoMostra e li inserisco nelle casse della Mostra.
     * Chiama in forward il metodo calcoloCostoIngresso che imposta il costo biglietto e imposta un insieme di Sale
     * per una Mostra
     */
    private void setMostra(boolean ancheVirtuale){
        Mostra mostra = new Mostra();
        incaricoAttuale.setMostra(this, mostra);
        mostra.addObserver(this);
        int rimanenze = incaricoAttuale.getBilancio();
        mostra.incassa(rimanenze);
        try {
            incaricoAttuale.prelevaDenaro(this, rimanenze);
        }catch (Exception e){;}
        setDetailMostra(ancheVirtuale);
        mostra.setOpereMostra(incaricoAttuale.getOpereMostra());
    }

    /**
     * Per calcolare il costo di ingresso elaboro quale sia la combinazione di Sale che posso avere e
     * in base a quello calcolo i soldi che deve costare il biglietto per rientrare con le spese.
     * Per ottenere quanti soldi sono stati spesi vedo il valore dei fondiStaziati e vedo quanti soldi sono rimasti
     * in Mostra. Ogni Stanza può esporre un'Opera di quelle affittate, che sia SalaFisica o SalaVirtuale.
     */
    private void setDetailMostra(boolean ancheVirtuale){
        Mostra mostra = incaricoAttuale.getMostra();
        Iterator<Sala> iterVirtuali = null;

        List<Sala> saleFisiche = museo.getSale(false);
        Iterator<Sala> iterFisiche = saleFisiche.iterator();

        if(ancheVirtuale) {
            List<Sala> saleVirtuali = museo.getSale(true);
            iterVirtuali = saleVirtuali.iterator();
        }

        Set<Sala> saleMostra = new HashSet<>();
        List<Opera> opereDaRestituire = new ArrayList<>();
        Iterator<Opera> iterOpere = incaricoAttuale.getOpereMostra().iterator();
        while(iterOpere.hasNext()){
            Opera o = iterOpere.next();
            if(iterFisiche.hasNext()){
                Sala s = iterFisiche.next();
                s.inserisciOpera(o);
                saleMostra.add(s);
                if(ancheVirtuale) {
                    if(iterVirtuali.hasNext()) {
                        Sala sv = iterVirtuali.next();
                        sv.associaSalaFisica(s);
                        saleMostra.add(sv);
                    }
                }
            }
            else{
                System.err.println("Non ci sono abbastanza sale libere per tutte le opere. Le opere non" +
                        "allocate verranno restituite");
                opereDaRestituire.add(o);
            }
        }
        GestoreOpere go = museo.getGestoreOpere();
        for(Opera o: opereDaRestituire) {
            go.restituisciOperaAffittata(o);
            incaricoAttuale.getOpereMostra().remove(o);
        }
        int count = 0;
        for(Sala s:saleMostra) {
            s.setBusy();
            count += s.getPostiSala();
        }
        int fondiStanziati = incaricoAttuale.getFondiStanziati();
        int rimanenzaFondiStanziati = fondiStanziati - mostra.getIncasso();
        int costoBiglietto = (int)((double)rimanenzaFondiStanziati/(double)count);
        costoBiglietto += 5;
        mostra.setPostiTotali(count);
        mostra.setSale(saleMostra, ancheVirtuale);
        mostra.setCostoTicket(costoBiglietto);
        mostra.setIncassoObiettivo(costoBiglietto * count);
        mostra.setOrganizzatore(this);
    }

    private void svolgiCompitiImpiegati(){
        for(Personale impiegato:incaricoAttuale.getImpiegati())
            impiegato.svolgiIncaricoAssegnato();
    }

    /**
     * Libera le opere che sono state noleggiate e le opere che erano occupate. Imposta questo incarico come terminato.
     * Stacca l'observer, e resetta le Sale.
     */
    private void chiudiMostra(){
        Mostra mostra = incaricoAttuale.getMostra();
        GestoreOpere gestoreOpere = museo.getGestoreOpere();
        for(Opera o: incaricoAttuale.getOpereMostra())
            gestoreOpere.restituisciOperaAffittata(o);
        for(Personale pp:incaricoAttuale.getImpiegati()) {
            pp.setFree();
            try {
                pp.setIncaricoImpiegato(null);
            } catch (Exception e) { }
        }

        Set<Sala> saleMostra = mostra.getSale();
        for(Sala sala: saleMostra) {
            sala.freeSala();
            sala.dissociaSalaFisica();
            sala.rimuoviOpera();
        }
        this.setFree();
        mostra.setTerminata();
        mostra.deleteObserver(this);
        incaricoAttuale.kill();
        museo.chiudiMostra(this, mostra);
    }

    /**
     * Metodo chiamato dall'Amministratore tramite l'Observer
     */
    private void chiusuraForzata(){
        stornaDenaroVisitatori();
        chiudiMostra();
    }

    private void stornaDenaroVisitatori(){
        Mostra mostra = incaricoAttuale.getMostra();
        List<Acquirente> visitatori = mostra.getVisitatoriMostra();
        int costoBiglietto = mostra.getCostoTicket();
        int parteDaStornare = calcolaStorno(costoBiglietto, 0.4f);
        for(Acquirente visitatore: visitatori)
            visitatore.ottieniRimborso(parteDaStornare);
    }

    private int calcolaStorno(int costoBiglietto, double trattenuta){
        double rimborso = costoBiglietto;
        rimborso -= rimborso*trattenuta;
        return (int) rimborso;
    }

    /**
     * Questo Observer osserva contemporaneamente sia l'IncaricoMostra che la Mostra, e quando questo metodo viene
     * chiamato dal notifyObserver degli Observable, sono sicuro che sia sempre per richiedere la chiusura della mostra.
     *
     * @param o - IncaricoMostra || Mostra
     * @param arg - Null
     */
    @Override
    public void update(Observable o, Object arg) {
        Mostra m = incaricoAttuale.getMostra();
        // Se o!= m allora è l'Amministratore
        if(o != m)
            chiusuraForzata();
        // altrimenti è la Mostra che si sta osservando.
        else
            chiudiMostra();
    }
}
