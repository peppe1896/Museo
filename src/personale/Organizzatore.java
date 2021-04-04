package personale;

import museo.Mostra;
import museo.Museo;
import museo.Sala;
import opera.GestoreOpere;
import opera.Opera;
import personale.pkgIncaricoImpiegato.CreaPubblicità;
import personale.pkgIncaricoImpiegato.PulizieSalaFisica;
import personale.pkgIncaricoImpiegato.spostareOpera;
import personale.pkgIncaricoMostra.IncaricoMostra;

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

    @Override
    public void svolgiIncaricoAssegnato() {
        organizzaMostra();
    }

    /**
     * Chiama in forward i metodi delle singole operazioni.
     * Aggiungo la mostra prendendo il riferimento alla mostra dall'Incarico.
     * Questo metodo è chiamato
     */
    private void organizzaMostra(){
        scegliImpiegati();
        pagaImpiegati();
        affittaOpere();
        assegnaCompiti();
        setMostra();
        museo.addMostra(incaricoAttuale.getMostra());
        svolgiCompitiImpiegati();

    }

    /**
     * Sceglie gli impiegati da far lavorare nella mostra e li imposta come occupati. Imposta questo ArrayList
     * all'IncaricoMostra. Il valore 3 perché prevedo ancora solo 3 Incarichi per i dipendenti
     */
    private void scegliImpiegati(){
        ArrayList<Personale> impiegati = new ArrayList<>();
        for(Personale p:museo.getImpiegati(true))
            if(impiegati.size() < 3)
                if(!p.isBusy()){
                    p.setBusy();
                    impiegati.add(p);
                }
        incaricoAttuale.setImpiegati(impiegati);
    }

    /**
     * Affitta le opere pagando il prezzo di noleggio
     */
    private void affittaOpere(){
        GestoreOpere gestoreOpere = museo.getGestoreOpere();
        for(Opera opera:incaricoAttuale.getOpereMostra()) {
            gestoreOpere.affittaOperaAMuseo(opera, museo);
            incaricoAttuale.prelevaDenaro(this, opera.getCostoNoleggio());
        }
    }

    /**
     * Pago gli impiegati e l'organizzatore: 10 gli impiegati e 20 l'organizzatore.
     */
    private void pagaImpiegati(){
        this.pagami(20);
        incaricoAttuale.prelevaDenaro(this, 20);
        for(Personale p:incaricoAttuale.getImpiegati()) {
            p.pagami(10);
            incaricoAttuale.prelevaDenaro(this, 10);
        }
    }

    private void assegnaCompiti() {
        IncaricoImpiegato incarichi[] = new IncaricoImpiegato[3];
        incarichi[0] = new CreaPubblicità();
        incarichi[1] = new spostareOpera();
        incarichi[2] = new PulizieSalaFisica();
        int index = 0;
        for(Personale p: incaricoAttuale.getImpiegati()){
            try {
                p.setIncaricoImpiegato(incarichi[index]);
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
    private void setMostra(){
        Mostra mostra = new Mostra();
        incaricoAttuale.setMostra(this, mostra);
        mostra.addObserver(this);
        int rimanenze = incaricoAttuale.getBilancio();
        mostra.incassa(rimanenze);
        incaricoAttuale.prelevaDenaro(this, rimanenze);
        museo.addMostra(mostra);
        calcolaCostoIngresso();
        mostra.setOpereMostra(incaricoAttuale.getOpereMostra());
    }

    /**
     * Per calcolare il costo di ingresso elaboro quale sia la combinazione di Sale che posso avere e
     * in base a quello calcolo i soldi che deve costare il biglietto per rientrare con le spese.
     * Per ottenere quanti soldi sono stati spesi vedo il valore dei fondiStaziati e vedo quanti soldi sono rimasti
     * in Mostra.
     */
    private void calcolaCostoIngresso(){
        Mostra mostra = incaricoAttuale.getMostra();
        List<Sala> saleMuseo = museo.getSale(false);
        Set<Sala> saleMostra = new HashSet<>();
        for(int i=0;i<4;i++){
            saleMostra.add(saleMuseo.get((i)));
        }
        int count = 0;
        for(Sala s:saleMostra) {
            count += s.getPostiSala();
            s.setBusy();
        }
        int differenza = incaricoAttuale.fondiStanziati - mostra.getIncasso();
        int costoBiglietto = differenza/count;
        costoBiglietto += 5;
        mostra.setPostiTotali(count);
        mostra.setSale(saleMostra);
        mostra.setCostoBiglietto(costoBiglietto);
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
        incaricoAttuale.kill();
        for(Opera o: incaricoAttuale.getOpereMostra())
            gestoreOpere.restituisciOperaAffittata(o);
        for(Personale pp:incaricoAttuale.getImpiegati()) {
            pp.setFree();
            try {
                pp.setIncaricoImpiegato(null);
            }catch (Exception e){}
        }
        mostra.deleteObserver(this);
        museo.addBilancio(this, mostra.svuotaCasse());
        Set<Sala> saleMostra = mostra.getSale();
        for(Sala sala: saleMostra)
            sala.freeSala();
        this.setFree();
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
        chiudiMostra();
    }
}
