package personale;

import museo.Museo;
import opera.GestoreOpere;
import opera.Opera;
import personale.pkgIncaricoImpiegato.CreaPubblicità;
import personale.pkgIncaricoImpiegato.PulizieSalaFisica;
import personale.pkgIncaricoImpiegato.spostareOpera;
import personale.pkgIncaricoMostra.IncaricoMostra;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Organizzatore sviluppa un IncaricoMostra fornito da un amministratore. se fosse richiesto dall'Amministratore,
 * questa mostra deve essere annullata rilasciando tutte le opere, e questa richiesta è fatta usando l'observer.
 */

public class Organizzatore extends Personale implements Observer {
    private Museo museo;
    private IncaricoMostra incaricoAttuale;

    public Organizzatore(Museo museo){
        this.museo = museo;
    }

    public void setIncaricoAttuale(IncaricoMostra incaricoMostra){
        incaricoAttuale = incaricoMostra;
        incaricoAttuale.addObserver(this);
    }

    /**
     * Chiama in forward i metodi delle singole operazioni. Vanno implementate con observer alcune?
     */
    public void organizzaMostra(){
        scegliImpiegati();
        pagaImpiegati();
        affittaOpere();
        assegnaCompiti();
    }

    /**
     * Sceglie gli impiegati da far lavorare nella mostra e li imposta come occupati. Imposta questo ArrayList
     * all'IncaricoMostra.
     */
    private void scegliImpiegati(){
        ArrayList<Personale> impiegati = new ArrayList<>();
        for(Personale p:museo.getImpiegati(true))
            if(impiegati.size() < 3)
                if(!p.isBusy()){
                    p.setOccupato();
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
     * Libera le opere che sono state noleggiate e le opere che erano occupate. Imposta questo incarico come terminato.
     */
    private void chiudiMostra(){
        GestoreOpere gestoreOpere = museo.getGestoreOpere();
        incaricoAttuale.kill();
        for(Opera o: incaricoAttuale.getOpereMostra())
            gestoreOpere.restituisciOperaAffittata(o);
    }

    @Override
    public void update(Observable o, Object arg) {
        chiudiMostra();
    }
}
