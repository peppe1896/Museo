package personale;

import museo.Museo;
import opera.Opera;

import java.util.ArrayList;

/**
 * Questa Strategy prende semplicemente 5 quadri da qualsiasi museo. Con questa strategy affitto 4 quadri e prendo
 * uno dal museo.
 */
public class HighBudgetStrategy implements Strategy{
    @Override
    public IncaricoMostra createIncaricoMostra(Museo museo){
        int numeroOpereHighStrategy = 5;
        IncaricoMostra incarico = new IncaricoMostra(300);
        ArrayList<Opera> opere = new ArrayList<>();
        ArrayList<Opera> opereMuseo = (ArrayList<Opera>) museo.getOpereMuseo();
        opere.add(opereMuseo.get(0));
        for(Opera o:museo.getCatalogoOpere()) {
            if(opere.size() < numeroOpereHighStrategy)
                if(!opereMuseo.contains(o))
                    opere.add(o);
        }
        incarico.setOpereMostra(opere);
        return incarico;
    }
}
