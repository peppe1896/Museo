package museo.ticket;

import museo.TicketMuseo;
import visitatore.Visitatore;

public class TicketMostraFisica implements TicketMuseo {
    Visitatore acquirente;
    public TicketMostraFisica(Visitatore visitatore) {
        this.acquirente = visitatore;
    }
}
