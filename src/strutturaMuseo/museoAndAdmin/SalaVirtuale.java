package strutturaMuseo.museoAndAdmin;

import strutturaMuseo.organizzazione.organizzatore.Sala;

public final class SalaVirtuale extends Sala {
    private SalaFisica sala;
    SalaVirtuale(int postiSala){
        super();
        this.postiSala = postiSala;
    }
}
