package museo;

import museo.sale.Sala;
import museo.sale.SalaFisica;
import museo.sale.SalaVirtuale;
import opera.Opera;
import personale.Organizzatore;
import visitatore.Acquirente;

import java.util.*;

/**
 * Una mostra è un Observable osservato dal suo Organizzatore. La mostra ha un numero di posti massimo
 * e quando viene riempito manda una notifica all'organizzatore che provvede a chiuderla e a incassare.
 */
public class Mostra extends Observable {
    private String name;
    Set<Sala> sale;
    private boolean ancheOnline = false;
    List<Opera> opereMostra;
    private int incasso = 0;
    private int costoBiglietto;
    private int bigliettiVenduti = 0;
    private int postiTotali;
    private int incassoObiettivo;
    private List<Acquirente> visitatoriMostra = new ArrayList<>();
    private boolean terminata = false;
    private Organizzatore organizzatore;
    private int postiFisici = 0;
    private int postiVirtuali = 0;


    public Mostra(){
        Random rnd = new Random();
        name = "Mostra " + (char)('a' + rnd.nextInt(26))+
                (char)('a' + rnd.nextInt(26))+
                (char)('a' + rnd.nextInt(26))+
                (char)('a' + rnd.nextInt(26))+
                (char)('a' + rnd.nextInt(26));
    }

    public void incassa(int incassa){
        this.incasso += incassa;
    }

    public void incassaBiglietto(Acquirente v) {
        bigliettiVenduti++;
        this.incasso += this.costoBiglietto;
        addVisitatore(v);
        checkRaggiuntoIncasso();
    }

    private void addVisitatore(Acquirente visitatore){
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
            System.out.println("Raggiunto incasso obiettivo");
            setChanged();
            notifyObservers();
        }
    }

    /**
     * Ci serve per fare l'eventuale Storno dei biglietti.
     * @return i Visitatori che hanno pagato il biglietto.
     */
    public List<Acquirente> getVisitatoriMostra(){
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

    public void togliPostoFisico(){
        postiFisici--;
        postiTotali--;
    }

    public void togliPostoVirtuale(){
        postiVirtuali--;
        postiTotali--;
    }

    public String getName(){
        return name;
    }
}
