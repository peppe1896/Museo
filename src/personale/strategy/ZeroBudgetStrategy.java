package personale.strategy;

import museo.Museo;

public class ZeroBudgetStrategy implements Strategy {
    /**
     * Non essendoci soldi, non è possibile finanziare una Mostra.
     * @param museo Serve per dare le informazioni necessarie alla strategy.
     * @return
     */
    @Override
    public IncaricoMostra creaIncaricoMostra(Museo museo) {
        return null;
    }
}
