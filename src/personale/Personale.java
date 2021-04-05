package personale;

import museo.Suggeritore;

/**
 * La classe astratta che viene estesa in Organizzatore ed Impiegato.
 */
public abstract class Personale implements Suggeritore {
    private boolean busyInQualcheMostra = false;
    private int portafogli = 0;

    public boolean isBusy(){
        return busyInQualcheMostra;
    }

    public void setBusy(){
        busyInQualcheMostra = true;
    }

    public void setFree(){
        busyInQualcheMostra = false;
    }

    public void pagami(int sommaPagamento){
        portafogli += sommaPagamento;
    }

    public void setIncaricoImpiegato(IncaricoImpiegato incaricoImpiegato) throws Exception{
        throw new Exception("Non si imposta un IncaricoImpiegato a un Organizzatore");
    }

    public abstract void svolgiIncaricoAssegnato();

    public int getPortafogli(){
        return portafogli;
    }
}
