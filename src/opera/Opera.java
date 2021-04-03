package opera;

import museo.Museo;

public class Opera {
    private String nome;
    private String autore;
    private Museo proprietario;

    //TODO: questo costruttore deve essere o privato o protetto.
    // Il catalogo delle opere d'arte va reso meno flessibile
    // potrebbe essere necessario dividere il proprietario dall'utilizzatore
    public Opera(String nome, String autore, Museo museoProprietario){
        this.nome = nome;
        this.autore = autore;
        this.proprietario = museoProprietario;
    }

    public String getNome(){
        return nome;
    }
}
