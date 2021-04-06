package personale.pkgIncaricoMostra;

import museo.Museo;

public class NoCreationStrategy implements Strategy {
    NoCreationStrategy(){}
    /**
     * Non essendoci soldi, non è possibile finanziare una Mostra.
     * @param museo Serve per dare le informazioni necessarie alla strategy.
     * @return
     */
    @Override
    public IncaricoMostra strategyMethod(Museo museo) {
        return null;
    }
}
