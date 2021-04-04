package museo;

import museo.sale.SalaVirtuale;
import opera.Opera;

import java.util.List;
import java.util.Observable;
import java.util.Set;

/**
 * Una mostra è un Observable osservato dal suo Organizzatore. La mostra ha un numero di posti massimo
 * e quando viene riempito manda una notifica all'organizzatore che provvede a chiuderla e a incassare.
 */
public class Mostra extends Observable {
    Set<Sala> sale;
    private boolean ancheOnline = false;
    List<Opera> opereMostra;
    private int incasso = 0;
    private int costoBiglietto;
    private int bigliettiVenduti = 0;
    private int postiTotali;

    /**
     * Incassa aggiunge soldi alle casse della Mostra, se presente almeno 1 posto disponibile per la Mostra.
     * Il return ci dice se l'operazione è andata a buon fine
     * @param incasso Soldi da aggiungere all'incasso della Mostra.
     * @return true ci sono posti per la mostra
     */
    public boolean incassa(int incasso){
        if(postiTotali>0) {
            this.incasso += incasso;
            postiTotali--;
            bigliettiVenduti++;
            return true;
        }
        return false;
    }

    public void setCostoBiglietto(int costoBiglietto){
        this.costoBiglietto = costoBiglietto;
    }

    public int getCostoBiglietto(){
        return costoBiglietto;
    }

    /**
     * Conto quanti posti ho disponibili nelle sale, e li inserisco come posti massimi della Mostra.
     * Inoltre capisco se una di queste sale è virtuali, e imposto il parametro ancheOnline di conseguenza.
     * @param sale Le Sale che sono state dedicate a questa Mostra.
     */
    public void setSale(Set<Sala> sale){
        this.sale = sale;
        for(Sala s:sale)
            if(s instanceof SalaVirtuale)
                ancheOnline = true;
    }

    public void setOpereMostra(List<Opera> opereMostra){
        this.opereMostra = opereMostra;
    }
    /**
     * Da attivare in fase di chiusura. Svuota le casse della Mostra. L'Organizzatore ha il compito di prendere
     * questo dato e di aggiungerlo al budget del Museo.
      * @return i soldi guadagnati dalla mostra
     */
    public int svuotaCasse(){
        int temp = incasso;
        incasso = 0;
        return temp;
    }

    public Set<Sala> getSale(){
        return sale;
    }

    public boolean isVirtual(){
        return ancheOnline;
    }

    public int getIncasso(){
        return incasso;
    }

    public void setPostiTotali(int postiTotali){
        this.postiTotali = postiTotali;
    }
}
