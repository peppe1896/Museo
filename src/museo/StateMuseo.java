package museo;

/**
 * Ci sono alcune informazioni sullo stato del museo, che vengono mandate all'Amministratore tramite
 * l'Observer push tra Museo e Amministratore.
 */
public class StateMuseo {
    private Suggerimento suggerimento;
    private int bilancioMuseo;
    private double loadFactorSale;
    private Mostra mostraChiusa;

    StateMuseo(Suggerimento suggerimento, int bilancioMuseo, double loadFactorSale, Mostra mostraChiusa){
        this.suggerimento = suggerimento;
        this.bilancioMuseo = bilancioMuseo;
        this.loadFactorSale = loadFactorSale;
        this.mostraChiusa = mostraChiusa;
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
}
