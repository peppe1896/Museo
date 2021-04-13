package strutturaMuseo.museo;

import strutturaMuseo.Suggeritore;
import opera.Opera;

public final class Suggerimento {
    private Opera suggerimento;
    private Suggeritore suggeritore;

    /**
     * Sarebbe package-private. Necessario public per fare i test
     * @param o
     * @param s
     */
    public Suggerimento(Opera o, Suggeritore s){
        this.suggerimento = o;
        this.suggeritore = s;
    }

    public Opera getSuggerimento(){
        return suggerimento;
    }
}
