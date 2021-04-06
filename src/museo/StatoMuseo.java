package museo;

/**
 * Ci sono alcune informazioni sullo stato del museo, che vengono mandate all'Amministratore tramite
 * l'Observer push tra Museo e Amministratore.
 */
public class StatoMuseo {
    private Suggerimento suggerimento;
    private int bilancioMuseo;
    private double loadFactorSale;
    private Mostra mostraChiusa;
    private boolean mostraAggiunta;

    StatoMuseo(Suggerimento suggerimento, int bilancioMuseo, double loadFactorSale, Mostra mostraChiusa, boolean mostraAggiunta){
        this.suggerimento = suggerimento;
        this.bilancioMuseo = bilancioMuseo;
        this.loadFactorSale = loadFactorSale;
        this.mostraChiusa = mostraChiusa;
        this.mostraAggiunta = mostraAggiunta;
    }

    public Suggerimento getOperaSuggerita(){
        return suggerimento;
    }

    public int getBilancioMuseo(){
        return bilancioMuseo;
    }

    public double getLoadFactorSale() {
        return loadFactorSale;
    }

    public Mostra getMostraChiusa(){
        return mostraChiusa;
    }

    /**
     * Dice se è stata aggiunta una Mostra al Set delle Mostre del Museo. Serve all'Amministratore per capire se può
     * creare un'altra Mostra.
     * @return true se è stata aggiunta una Mostra.
     */
    public boolean getMostraAggiunta(){
        return mostraAggiunta;
    }
}
