package strutturaMuseo.museoAndAdmin;

import visitatore.Acquirente;

public final class Biglietto implements Ticket {
    Acquirente acquirente;
    Biglietto(Acquirente visitatore){
        this.acquirente = visitatore;
    }
}
