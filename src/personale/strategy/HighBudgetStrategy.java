package personale.strategy;

import museo.Museo;
import opera.GestoreOpere;
import opera.Opera;

import java.util.ArrayList;

/**
 * Questa Strategy prende 4 quadri da qualsiasi museo e prendo uno dal museo passato come parametro.
 * <p>
 * Questi metodi creaIncaricoMostra compongono l'ArrayList {@code opereNuovoIncarico} mettendo prima le opere del museo, e poi
 * quelle degli altri musei che verranno affittate.
 * @return  IncaricoMostra da dare all'amministratore, che successivamente lo da a un organizzatore.
 *
 */
public class HighBudgetStrategy implements Strategy {
    @Override
    public IncaricoMostra creaIncaricoMostra(Museo museo){
        GestoreOpere gestoreOpere = museo.getGestoreOpere();
        int numeroOpereHighStrategy = 5;
        IncaricoMostra incarico = new IncaricoMostra(300);
        ArrayList<Opera> opereNuovoIncarico = new ArrayList<>();
        ArrayList<Opera> opereMuseo = (ArrayList<Opera>) museo.getOpereMuseo();
        // Voglio solo un'opera del Museo proprietario, quindi metto la prima opera libera
        for(Opera operaMuseo:opereMuseo) {
            if(!operaMuseo.isBusy() && opereNuovoIncarico.size() == 0)
                opereNuovoIncarico.add(operaMuseo);
        }
        for(Opera o:museo.getCatalogoOpere()) {
            if(opereNuovoIncarico.size() < numeroOpereHighStrategy)
                if(!o.isBusy() && o.getProprietario() != museo) {
                    gestoreOpere.affittaOperaAMuseo(o, museo);
                    opereNuovoIncarico.add(o);
                }
        }
        incarico.setOpereMostra(opereNuovoIncarico);
        return incarico;
    }
}
