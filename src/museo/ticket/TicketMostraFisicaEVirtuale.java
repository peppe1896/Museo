package museo.ticket;

import museo.TicketMuseo;
import visitatore.Visitatore;

public class TicketMostraFisicaEVirtuale implements TicketMuseo {
    Visitatore acquirente;
    public TicketMostraFisicaEVirtuale(Visitatore visitatore){
        this.acquirente = visitatore;
    }
}
