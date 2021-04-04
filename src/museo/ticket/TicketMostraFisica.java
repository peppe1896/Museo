package museo.ticket;

import visitatore.Visitatore;

public class TicketMostraFisica implements TicketMuseo {
    Visitatore acquirente;
    public TicketMostraFisica(Visitatore visitatore) {
        this.acquirente = visitatore;
    }
}
