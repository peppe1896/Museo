package strutturaMuseo.personaleMuseo.amministratore;

import strutturaMuseo.museo.Museo;
import opera.GestoreOpere;
import opera.Opera;
import strutturaMuseo.personaleMuseo.NoMoneyException;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Questa Strategy prende 4 quadri da qualsiasi museo e prendo uno dal museo passato come parametro.
 * <p>
 * Questi metodi creaIncaricoMostra compongono l'ArrayList {@code opereNuovoIncarico} mettendo prima le opere del museo, e poi
 * quelle degli altri musei che verranno affittate.
 * @return  IncaricoMostra da dare all'amministratore, che successivamente lo da a un organizzatore.
 *
 */
class PersonalStrategy implements Strategy {
    private final int numeroOpere;
    private final boolean ancheVirtuale;

    PersonalStrategy(int numeroOpere, boolean ancheVirtuale){
        this.numeroOpere = numeroOpere;
        this.ancheVirtuale = ancheVirtuale;
    }

    public IncaricoMostra strategyMethod(Museo museo){
        GestoreOpere gestoreOpere = museo.getGestoreOpere();
        ArrayList<Opera> opereNuovoIncarico = new ArrayList<>();
        ArrayList<Opera> opereMuseo = (ArrayList<Opera>) museo.getOpereMuseo();
        int media = 0;
        for(Opera opera:opereMuseo)
            media += opera.getCostoNoleggio();
        media = media/opereMuseo.size();                        //    costo opere + operai +  sicurezza
        IncaricoMostra incarico = new IncaricoMostra(numeroOpere*media + 60 + numeroOpere*10, ancheVirtuale);
        // Voglio solo un'opera del Museo proprietario, quindi metto la prima opera libera
        Iterator<Opera> iterator = opereMuseo.iterator();
        while(opereNuovoIncarico.size() == 0 && iterator.hasNext()){
            Opera opera = iterator.next();
            if(!opera.isBusy()) {
                gestoreOpere.affittaOperaAMuseo(opera, incarico, museo);
                opereNuovoIncarico.add(opera);
            }
        }
        iterator = museo.getCatalogoOpere().iterator();
        while(iterator.hasNext() && opereNuovoIncarico.size() < numeroOpere) {
            Opera opera = iterator.next();
            if (!opera.isBusy() && opera.getProprietario() != museo) {
                try {
                    museo.prelevaBilancio(this, opera.getCostoNoleggio());
                    gestoreOpere.affittaOperaAMuseo(opera, incarico, museo);
                    opereNuovoIncarico.add(opera);
                } catch (NoMoneyException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
        incarico.setOpereMostra(opereNuovoIncarico);
        return incarico;
    }
}
