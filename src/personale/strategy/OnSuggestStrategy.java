package personale.strategy;

import museo.Museo;
import opera.Opera;

import java.util.ArrayList;
import java.util.Set;

public class OnSuggestStrategy implements Strategy {
    private final Set<Opera> opereRichieste;

    OnSuggestStrategy(Set<Opera> opereRichieste){
        this.opereRichieste = opereRichieste;
    }

    /**
     * Questi metodi creaIncaricoMostra compongono l'ArrayList {@code opereNuovoIncarico} mettendo prima le opere del museo, e poi
     * quelle degli altri musei che verranno affittate.
     * @param museo Serve per dare le informazioni necessarie alla strategy.
     * @return  IncaricoMostra da dare all'amministratore, che successivamente lo da a un organizzatore.
     */
    @Override
    public IncaricoMostra creaIncaricoMostra(Museo museo) {
        int numeroOpereAutoStrategy = 5;
        IncaricoMostra incarico = new IncaricoMostra(200);
        ArrayList<Opera> opereNuovoIncarico = new ArrayList<>();
        ArrayList<Opera> opereMuseo = (ArrayList<Opera>) museo.getOpereMuseo();
        Set<Opera> catalogoOpere = museo.getCatalogoOpere();

        for(Opera operaMuseo:opereMuseo) {
            if(!operaMuseo.isBusy() && opereNuovoIncarico.size() < 3)
                opereNuovoIncarico.add(operaMuseo);
        }
        for(Opera o:opereRichieste) {
            if(opereNuovoIncarico.size() < numeroOpereAutoStrategy)
                if(o.getProprietario() != museo && !o.isBusy())
                    opereNuovoIncarico.add(o);
        }
        incarico.setOpereMostra(opereNuovoIncarico);
        return incarico;
    }
}
