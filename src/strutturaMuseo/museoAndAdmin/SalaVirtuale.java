package strutturaMuseo.museoAndAdmin;

import strutturaMuseo.organizzazione.organizzatore.Sala;

public final class SalaVirtuale extends Sala {
    private Sala sala = null;
    SalaVirtuale(int postiSala){
        super();
        this.postiSala = postiSala;
    }
    public void associaSalaFisica(Sala sala){
        this.sala = sala;
    }
    public void dissociaSalaFisica(){
        this.sala = null;
    }

    public Sala getSalaFisica(){
        return sala;
    }
}
