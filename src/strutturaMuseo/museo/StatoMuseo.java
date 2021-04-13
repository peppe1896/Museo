package strutturaMuseo.museo;

/**
 * Ci sono alcune informazioni sullo stato del museo, che vengono mandate all'Amministratore tramite
 * l'Observer push tra Museo e Amministratore.
 */
public final class StatoMuseo {
    private Suggerimento suggerimento;
    private Integer bilancioMuseo;
    private Double loadFactorSale;
    private Mostra mostraChiusa;
    private Boolean museoIsReady;

    StatoMuseo(Suggerimento suggerimento, Integer bilancioMuseo, Double loadFactorSale, Mostra mostraChiusa, Boolean museoIsReady){
        this.suggerimento = suggerimento;
        this.bilancioMuseo = bilancioMuseo;
        this.loadFactorSale = loadFactorSale;
        this.mostraChiusa = mostraChiusa;
        this.museoIsReady = museoIsReady;
    }

    public Suggerimento getOperaSuggerita(){
        return suggerimento;
    }

    public Integer getBilancioMuseo(){
        return bilancioMuseo;
    }

    public Double getLoadFactorSale() {
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
    public Boolean getMuseoIsReady(){
        return museoIsReady;
    }

    public void setLoadFactorSale(double newLoadFactor){
        this.loadFactorSale = newLoadFactor;
    }
}
