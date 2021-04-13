package strutturaMuseo.museo;

import visitatore.Acquirente;

public final class TicketMostraFisicaEVirtuale implements Ticket {
    Acquirente acquirente;
    TicketMostraFisicaEVirtuale(Acquirente visitatore){
        this.acquirente = visitatore;
    }
}
