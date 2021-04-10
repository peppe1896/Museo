package strutturaMuseo.personaleMuseo.amministratore;

import strutturaMuseo.museo.Museo;
import opera.GestoreOpere;
import opera.Opera;
import strutturaMuseo.personaleMuseo.NoMoneyException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

class OnSuggestStrategy implements Strategy {
    private Set<Opera> opereRichieste;
    private Amministratore amministratore;

    OnSuggestStrategy(Set<Opera> opereRichieste, Amministratore amministratore){
        this.opereRichieste = opereRichieste;
        this.amministratore = amministratore;
    }

    /**
     * Questi metodi creaIncaricoMostra compongono l'ArrayList {@code opereNuovoIncarico} mettendo prima le opere del museo, e poi
     * quelle degli altri musei che verranno affittate.
     * @param museo Serve per dare le informazioni necessarie alla strategy.
     * @return  IncaricoMostra da dare all'amministratore, che successivamente lo da a un organizzatore.
     */
    @Override
    public IncaricoMostra strategyMethod(Museo museo) {
        ArrayList<Opera> opereResetSuggerimenti = new ArrayList<>();
        GestoreOpere gestoreOpere = museo.getGestoreOpere();
        int numeroOpereAutoStrategy = 5;
        IncaricoMostra incarico = new IncaricoMostra(200, true);
        ArrayList<Opera> opereNuovoIncarico = new ArrayList<>();
        ArrayList<Opera> opereMuseo = (ArrayList<Opera>) museo.getOpereMuseo();
        Iterator<Opera> iterator = opereMuseo.iterator();
        while(opereNuovoIncarico.size() < 3 && iterator.hasNext()){
            Opera operaMuseo = iterator.next();
            if(!operaMuseo.isBusy())
                gestoreOpere.affittaOperaAMuseo(operaMuseo, incarico, museo);
                opereNuovoIncarico.add(operaMuseo);
        }
        iterator = opereRichieste.iterator();
        while(opereNuovoIncarico.size() < numeroOpereAutoStrategy && iterator.hasNext()){
            Opera opera = iterator.next();
                if(opera.getProprietario() != museo)
                    if(!opera.isBusy()) {
                        try {
                            museo.prelevaBilancio(this, opera.getCostoNoleggio());
                            gestoreOpere.affittaOperaAMuseo(opera, incarico, museo);
                            opereNuovoIncarico.add(opera);
                            opereResetSuggerimenti.add(opera);
                        } catch (NoMoneyException e) { System.err.println(e.getMessage());}
                    }
        }
        amministratore.resetSuggerimentiPerOpere(opereResetSuggerimenti);

        incarico.setOpereMostra(opereNuovoIncarico);
        return incarico;
    }
}
