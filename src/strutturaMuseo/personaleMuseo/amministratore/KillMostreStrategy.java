package strutturaMuseo.personaleMuseo.amministratore;

import strutturaMuseo.museo.Museo;

import java.util.Iterator;
import java.util.Set;

/**
 * Con questa strategy killo il primo IncaricoMostra (e in conseguenza anche la Mostra) corrispondente.
 * Lo strategy method ritorna null. Una strategia può uccidere solo una Mostra, visto che se c ene dovessero essere altre
 * l'Amministratore si preoccuperebbe di creare una nuova Strategy di questo tipo.
 */
class KillMostreStrategy implements Strategy{
    private Set<IncaricoMostra> incarichiMostre;
    private boolean strategyHasKilled = false;

    KillMostreStrategy(Set<IncaricoMostra> incarichiMostre){
        this.incarichiMostre = incarichiMostre;
    }
    @Override
    public IncaricoMostra strategyMethod(Museo museo) {
        Iterator<IncaricoMostra> iterator = incarichiMostre.iterator();
        while(!strategyHasKilled && iterator.hasNext()){
            IncaricoMostra temp = iterator.next();
            if(temp.isKillable()) {
                this.strategyHasKilled = true;
                temp.kill();
                temp.forzaChiusuraMostra();
                break;
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
