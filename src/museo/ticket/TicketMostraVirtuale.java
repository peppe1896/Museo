package museo.ticket;

import museo.TicketMuseo;
import visitatore.Visitatore;

public class TicketMostraVirtuale implements TicketMuseo {
    Visitatore acquirente;
    public TicketMostraVirtuale(Visitatore visitatore){
        this.acquirente = visitatore;
    }
}
