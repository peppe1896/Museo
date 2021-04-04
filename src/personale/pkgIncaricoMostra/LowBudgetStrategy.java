package personale.pkgIncaricoMostra;

import museo.Museo;
import opera.Opera;

import java.util.ArrayList;

public class LowBudgetStrategy implements Strategy {

    LowBudgetStrategy(){}

    /**
     * Strategy che stanzia una piccola somma per creare un IncaricoMostra, usando solo le opere che sono di proprietà
     * del museo passato come parametro da parte dell'Amministratore del Museo. Questo IncaricoMostra viene passato
     * all'amministratore, al quale assegnerà un Organizzatore.
     * <p>
     * @return Un IncaricoMostra
     * @param museo Il museo sotto controllo dell'Amministratore
     */
    //TODO voglio poter mettere anche un'opera a noleggio
    public IncaricoMostra strategyMethod(Museo museo){
        int numeroOpereLowStrategy = 3;
        IncaricoMostra incarico = new IncaricoMostra(50);
        ArrayList<Opera> opere = new ArrayList<>();
        for(Opera o:museo.getOpereMuseo())
            if(opere.size() <= numeroOpereLowStrategy)
                opere.add(o);
        incarico.setOpereMostra(opere);
        return incarico;
    }
}
