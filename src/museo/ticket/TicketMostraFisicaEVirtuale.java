package museo.ticket;

import museo.TicketMuseo;
import visitatore.Acquirente;
import visitatore.Visitatore;

public class TicketMostraFisicaEVirtuale implements TicketMuseo {
    Acquirente acquirente;
    public TicketMostraFisicaEVirtuale(Acquirente visitatore){
        this.acquirente = visitatore;
    }
}
