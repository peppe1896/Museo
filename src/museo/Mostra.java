package museo;

import museo.sale.Sala;
import museo.sale.SalaFisica;
import museo.sale.SalaVirtuale;
import opera.Opera;
import personale.Organizzatore;
import visitatore.Visitatore;

import java.util.ArrayList;
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
    private int incassoObiettivo;
    private List<Visitatore> visitatoriMostra = new ArrayList<>();
    private boolean terminata = false;
    private Organizzatore organizzatore;
    private int postiFisici = 0;
    private int postiVirtuali = 0;

    /**
     * Procedura per pagare il costo del biglietto. Verifica se si può acquistare un posto, e se si, aggiunge l'importo
     * del biglietto alla cassa della Mostra. Ritorna True se è riuscito a pagare
     * Più specificatamente.
     * Sto cercando un posto virtuale->se c'è un posto virtuale disponibile, incasso il costo del biglietto, tolgo
     * 1 ai posti virtuali disponibili e ritorno true, altrimenti se non ci sono posti virtuali disponibili, ritorno false.
     * Se il parametro è false, vuol dire che sto cercando un posto fisico. Stesso ragionamento.
     * @param visitatore Colui che sta comprando il Biglietto.
     * @param postoVirtuale se sto cercando un posto virtuale || false è per indicare un posto fisico
     * @return true se c'è un posto disponibile, e incasso il prezzo del biglietto.
     */
    public boolean pagaIngresso(Visitatore visitatore, boolean postoVirtuale){
        if (postoVirtuale) {
            if (acquistabile(postoVirtuale)){
                this.incasso += incasso;
                postiTotali--;
                bigliettiVenduti++;
                checkRaggiuntoIncasso();
                addVisitatore(visitatore);
                return true;
            }
        }else{
            if(acquistabile(!postoVirtuale)) {
                this.incasso += incasso;
                postiTotali--;
                bigliettiVenduti++;
                checkRaggiuntoIncasso();
                addVisitatore(visitatore);
                return true;
            }
        }return false;
    }
    public void incassa(int incassa){
        this.incasso += incassa;
    }
    /**
     * E' acquistabile un posto? Con il boolean chiedo se posso acquistare un posto virtuale, e con false
     * chiedo se posso acquistare un posto fisico.
     * @param postoFisico se true vedo se un postoFisico è acquistabile.
     * @return true se puoi comprarlo
     */
    private boolean acquistabile(boolean postoFisico){
        if(postoFisico) {
            if(postiFisici > 0) {
                postiFisici--;
                return true;
            }
        } else {
            if (postiVirtuali > 0) {
                postiVirtuali--;
                return true;
            }
        }
        return false;
    }

    private void addVisitatore(Visitatore visitatore){
        this.visitatoriMostra.add(visitatore);
    }

    public void setCostoBiglietto(int costoBiglietto){
        this.costoBiglietto = costoBiglietto;
    }

    public int getCostoBiglietto(){
        return costoBiglietto;
    }

    public void setSale(Set<Sala> sale, boolean ancheOnline){
        this.sale = sale;
        this.ancheOnline = ancheOnline;
        for(Sala sala:sale) {
            if (sala instanceof SalaVirtuale) {
                postiVirtuali += sala.getPostiSala();
            }
            if (sala instanceof SalaFisica) {
                postiFisici += sala.getPostiSala();
            }
        }
        if((postiFisici+postiVirtuali) != postiTotali)
            System.err.println("ERRORE: non c'è lo stesso numero di posti");
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

    public void setIncassoObiettivo(int incassoObiettivo){
        this.incassoObiettivo = incassoObiettivo;
    }

    /**
     * Se ho raggiunto l'incasso che mi sono prefissato, chiudo la mostra e ritorno le opere. Mando una notifica all'organizzatore
     * che la Mostra è finita così che lui possa avviare la procedura di chiusura.
     */
    private void checkRaggiuntoIncasso(){
        if(incasso >= incassoObiettivo) {
            setChanged();
            notifyObservers();
        }
    }

    /**
     * Ci serve per fare l'eventuale Storno dei biglietti.
     * @return i Visitatori che hanno pagato il biglietto.
     */
    public List<Visitatore> getVisitatoriMostra(){
        return visitatoriMostra;
    }

    public void setTerminata(){
        this.terminata = true;
    }

    public boolean isTerminata(){
        return terminata;
    }

    public void setOrganizzatore(Organizzatore organizzatore){
        this.organizzatore = organizzatore;
    }

    public Organizzatore getOrganizzatore(){
        return organizzatore;
    }

    public int getPostiRimasti(){
        return postiTotali;
    }

    public int getPostiFisiciRimasti(){
        return postiFisici;
    }

    public int getPostiVirtualiRimasti(){
        return postiVirtuali;
    }

}
