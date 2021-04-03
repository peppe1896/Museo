package opera;

import museo.Museo;

public final class Opera {
    private final String nome;
    private final String autore;
    private final Museo proprietario;
    private final int costoNoleggio;

    private Museo museoInMostra;
    private boolean busyInQualcheMostra;

    // TODO: questo costruttore deve essere o privato o protetto.
    // Il catalogo delle opere d'arte va reso meno flessibile

    /** Costruisce un'opera d'arte con un nome, un autore e un proprietario. Questo
     * costruttore sarebbe meglio implementarlo in un factory.
     *
     * @param nome Nome dell'opera d'arte.
     * @param autore Nome dell'autore.
     * @param museoProprietario Il museo che è proprietario di questa opera d'arte.
     */
    public Opera(String nome, String autore, Museo museoProprietario, int costoNoleggio){
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
    public boolean isBusy(){
        return busyInQualcheMostra;
    }
    public void affitta(Museo museoInMostra){
        this.museoInMostra = museoInMostra;
    }

}
