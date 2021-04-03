package personale;

import museo.Suggeritore;

public abstract class Personale implements Suggeritore {
    private boolean busyInQualcheMostra;

    public boolean isBusy(){
        return busyInQualcheMostra;
    }

    public void setOccupato(){
        busyInQualcheMostra = true;
    }

    public void setFree(){
        busyInQualcheMostra = false;
    }
}
