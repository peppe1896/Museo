package opera;

import museo.Museo;

/**
 * Descrive un'opera. E' parzialmente immutabile. Il costruttore package-protected permette solo al
 * GestoreOpere di crearle, il quale lo fa creando un Set a sua volta immutabile, da poter passare a chi lo richiede.
 * Lo richiedono i Musei per avere sempre disponibili le Opere, e i Suggeritori, cioè coloro che possono dare
 * suggerimenti per Mostre future.
 */
public final class Opera {
    private final String nome;
    private final String autore;
    private final int costoNoleggio;

    private Museo proprietario;
    private Museo museoInMostra;
    private boolean busyInQualcheMostra;

    /** Costruisce un'opera d'arte con un nome, un autore, un proprietario(un Museo) e un costo di noleggio. Questo
     * costruttore è package-protected: l'unico oggetto previsto che lo possa chiamare è il GestoreOpere, il quale crea
     * un Set di oggetti Opera immutabile, e richiedibile con il metodo getCatalogoOpere().
     *
     * @param nome Nome dell'opera d'arte.
     * @param autore Nome dell'autore.
     * @param museoProprietario Il museo che è proprietario di questa opera d'arte.
     */
    Opera(String nome, String autore, Museo museoProprietario, int costoNoleggio){
        this.nome = nome;
        this.autore = autore;
        this.proprietario = museoProprietario;
        this.costoNoleggio = costoNoleggio;

        busyInQualcheMostra = false;
        museoInMostra = proprietario;
    }

    public String getNome(){
        return nome;
    }
    public Museo getProprietario(){
        return proprietario;
    }
    public Museo getAffittuario(){
        return museoInMostra;
    }
    public boolean isBusy() {
        return busyInQualcheMostra;
    }
    public int getCostoNoleggio(){
        return costoNoleggio;
    }
    public String getAutore(){
        return autore;
    }

    /**
     * Queste operazioni sono quelle che può usare il GestoreOpere, il quale si occupa di affitare l'opera e di
     * rilasciarla.
     * @param museoInMostra : Imposta il Museo in cui viene esposto per qualche Mostra.
     */
    void affitta(Museo museoInMostra){
        this.museoInMostra = museoInMostra;
        this.busyInQualcheMostra = true;
    }
    void rilasciaOpera(){
        this.museoInMostra = proprietario;
        this.busyInQualcheMostra = false;
    }

    /**
     * Il proprietario deve rimanere immutato. Lo imposta il GestoreOpere.
     * @param proprietario
     */
    void setProprietario(Museo proprietario){
        this.proprietario = proprietario;
    }

}
