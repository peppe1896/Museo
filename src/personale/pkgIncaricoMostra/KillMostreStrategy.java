package personale.pkgIncaricoMostra;

import museo.Museo;

import java.util.Iterator;
import java.util.Set;

/**
 * Con questa strategy killo il primo IncaricoMostra (e in conseguenza anche la Mostra) corrispondente.
 * Lo strategy method ritorna null. Una strategia può uccidere solo una Mostra, visto che se c ene dovessero essere altre
 * l'Amministratore si preoccuperebbe di creare una nuova Strategy di questo tipo.
 */
public class KillMostreStrategy implements Strategy{
    private Set<IncaricoMostra> incarichiMostre;
    private Amministratore amministratore;
    private boolean strategyHasKilled = false;

    KillMostreStrategy(Set<IncaricoMostra> incarichiMostre, Amministratore amministratore){
        this.incarichiMostre = incarichiMostre;
        this.amministratore = amministratore;
    }
    @Override
    public IncaricoMostra strategyMethod(Museo museo) {
        System.out.println("~~~~Kill Strategy");
        Iterator<IncaricoMostra> iterator = incarichiMostre.iterator();
        while(!strategyHasKilled && iterator.hasNext()){
            IncaricoMostra temp = iterator.next();
            if(temp.isKillable()) {
                this.strategyHasKilled = true;
                temp.kill();
                temp.forzaChiusuraMostra();
            }
        }
        return null;
    }

    /**
     * Dici alla Strategy che può andare a caccia di un altro IncaricoMostra da terminare.
     */
    public void setReady(){
        this.strategyHasKilled = false;
    }

    public boolean isReady(){
        return !strategyHasKilled;
    }
}
