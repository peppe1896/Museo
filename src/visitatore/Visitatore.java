package visitatore;

import museo.Suggeritore;

public class Visitatore implements Suggeritore {
    private int bilancio = 0;

    public Visitatore(){}
    /**
     Chiamata da Museo per pagare
     **/
    public boolean paga(int soldi){
        if(bilancio>=soldi){
            bilancio -= soldi;
            return true;
        }
        return false;
    }

    public Visitatore(int bilancio){
        this.bilancio = bilancio;
    }

}
