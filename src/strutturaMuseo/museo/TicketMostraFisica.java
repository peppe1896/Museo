package strutturaMuseo.museo;

import visitatore.Acquirente;

public final class TicketMostraFisica implements Ticket {
    Acquirente acquirente;
    TicketMostraFisica(Acquirente visitatore) {
        this.acquirente = visitatore;
    }
}
