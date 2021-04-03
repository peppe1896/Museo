package museo;

import opera.Opera;

public class Suggerimento {
    private Opera suggerimento;
    private Suggeritore suggeritore;

    public Suggerimento(Opera o, Suggeritore s){
        this.suggerimento = o;
        this.suggeritore = s;
    }

    public Opera getSuggerimento(){
        return suggerimento;
    }
}
