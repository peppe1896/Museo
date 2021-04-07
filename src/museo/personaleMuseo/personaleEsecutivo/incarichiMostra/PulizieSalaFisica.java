package museo.personaleMuseo.personaleEsecutivo.incarichiMostra;

public class PulizieSalaFisica extends IncaricoImpiegato {
    @Override
    public boolean svolgiIncarico() {
        System.out.print("Pulizie fatte-");
        return true;
    }
}
