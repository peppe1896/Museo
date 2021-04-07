package visitatore;

import museo.ticket.TicketMuseo;
import museo.personaleMuseo.NoMoneyException;

public interface Acquirente {
    void paga(int somma) throws NoMoneyException;
    void addTicket(TicketMuseo ticketMuseo);
    void ottieniRimborso(int rimborso);
    int getBilancio();
}
