package personale.pkgIncaricoMostra;

import museo.Mostra;
import opera.Opera;
import personale.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**IncaricoMostra è un oggetto che viene creato da Amministratore
 * e che viene gestito da Organizzatore per organizzare una Mostra. Contiene tutte e informazioni
 * generali quali il budget istanziato e le opere da richiedere.
 * <p>
 * Quando l'amministratore assegna questo IncaricoMostra a un Organizzatore, l'Amministratore a quel punto lascia
 * la responsabilità di creare la Mostra all'organizzatore da lui assegnato. Inoltre, grazie all'implementazione di un
 * Observer tra IncaricoMostra e il suo Organizzatore, è possibile permettere il meccanismo di interruzione forzata della
 * creazione della Mostra, o l'annullamento della Mostra già creata: l'Amministratore può chiamare
 * il metodo {@code avviaProceduraChiusuraMostra()}, che forza l'avvio della funzione di chiusuraMostra di Organizzatore
 * , il quale provvederà a cancellare la Mostra. Attenzione: questa chiusura forzata può essere fatta solo sugli IncarichiMostre
 * che sono killabili (killable).
 */
public class IncaricoMostra extends Observable implements Incarico {
    private ArrayList<Opera> opereMostra;
    private int bilancio;
    private Personale organizzatore;
    private ArrayList<Personale> impiegati;
    private boolean killable = true;
    private Mostra mostra = null;
    public final int fondiStanziati;

    IncaricoMostra(int bilancioMostra){
        opereMostra = new ArrayList<>();
        bilancio = bilancioMostra;
        fondiStanziati = bilancioMostra;
    }

    /**
     * Metodi che usa l'Amministratore
     */
    public void setOrganizzatore(Organizzatore organizzatore){
        this.organizzatore = organizzatore;
    }
    public void setOpereMostra(List<Opera> opere){
        opereMostra = (ArrayList<Opera>) opere;
    }
    public void avviaProceduraChiusuraMostra(Object requester){
        if(requester instanceof Amministratore) {
            setChanged();
            notifyObservers();
        }
    }

    /**
     * Metodi che usa l'Organizzatore
     */
    public void setImpiegati(ArrayList<Personale> impiegati){
        this.impiegati = impiegati;
    }
    public ArrayList<Personale> getImpiegati(){
        return impiegati;
    }
    public void setMostra(Organizzatore organizzatore, Mostra mostra){
        if(this.organizzatore == organizzatore)
            this.mostra = mostra;
    }
    public Mostra getMostra(){
        return mostra;
    }
    /**
     * Preleva il denaro dal fondi stanziati per questa Mostra
     * @param denaroRichiesto Il denaro da prelevare
     */
    public void prelevaDenaro(Object requester, int denaroRichiesto){
        if(requester == organizzatore) {
            try {
                if (denaroRichiesto <= bilancio)
                    bilancio -= denaroRichiesto;
            } catch (Exception e) {
                System.err.println("Non ci sono abbastanza fondi");
            }
        }
    }

    public void kill(){
        this.killable = false;
    }

    @Override
    public boolean svolgiIncarico() {
        // TODO da implementare lo svolgiIncarico di IncaricoMostra
        return true;
    }

    /**
     * Metodi Generali
     */
    public int getBilancio(){
        return bilancio;
    }
    public List<Opera> getOpereMostra(){
        return opereMostra;
    }

    /**
     *
     * @return true se è ancora attiva e può quindi essere killata.
     */
    public boolean isKillable(){
        return killable;
    }

    public Personale getOrganizzatore(){
        return organizzatore;
    }
}
