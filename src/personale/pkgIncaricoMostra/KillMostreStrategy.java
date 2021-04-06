package personale.pkgIncaricoMostra;

import museo.Museo;

import java.util.Set;

/**
 * Con questa strategy killo il primo IncaricoMostra (e in conseguenza anche la Mostra) corrispondente.
 * Lo strategy method ritorna null.
 */
public class KillMostreStrategy implements Strategy{
    private Set<IncaricoMostra> incarichiMostre;
    private Amministratore amministratore;
    KillMostreStrategy(Set<IncaricoMostra> incarichiMostre, Amministratore amministratore){
        this.incarichiMostre = incarichiMostre;
        this.amministratore = amministratore;
    }
    @Override
    public IncaricoMostra strategyMethod(Museo museo) {
        for(IncaricoMostra im: incarichiMostre)
            if(im.isKillable()) {
                im.forzaChiusuraMostra(amministratore);
            }
        return null;
    }
}
