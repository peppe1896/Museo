package strutturaMuseo.personaleMuseo.amministratore;

import strutturaMuseo.museo.Museo;

class IdleStrategy implements Strategy {
    IdleStrategy(){}

    @Override
    public IncaricoMostra strategyMethod(Museo museo) {
        return null;
    }
}
