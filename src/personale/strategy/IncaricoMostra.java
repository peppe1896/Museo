package personale.strategy;

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
 * , il quale provvederà a cancellare la Mostra.
 */
public class IncaricoMostra extends Observable implements Incarico {
    private ArrayList<Opera> opereMostra;
    private int bilancio;
    private Personale organizzatore;
    private ArrayList<Personale> impiegati;

    IncaricoMostra(int bilancioMostra){
        opereMostra = new ArrayList<>();
        bilancio = bilancioMostra;
        impiegati = new ArrayList<>();
    }

    public void setOrganizzatore(Organizzatore organizzatore){
        this.organizzatore = organizzatore;
    }
    public void setOpereMostra(List<Opera> opere){
        opereMostra = (ArrayList<Opera>) opere;
    }
    public void addImpiegato(Impiegato impiegato){
        impiegati.add(impiegato);
        impiegato.setOccupato();
    }
    public int getBilancio(){
        return bilancio;
    }
    public void prelevaDenaro(int denaroRichiesto){
        try {
            if (denaroRichiesto < bilancio)
                bilancio -= denaroRichiesto;
        } catch (Exception e){
            System.err.println("Non ci sono abbastanza fondi");
        }
    }
    public List<Opera> getOpereMostra(){
        return opereMostra;
    }
    public void pagaPersonale(Object requester, Personale daPagare){
        //TODO implementa. Voglio che il requester sia solo l'organizzatore.
    }
    public void avviaProceduraChiusuraMostra(Object requester){
        if(requester instanceof Amministratore) {
            setChanged();
            notifyObservers();
        }
    }
}
