package personale.pkgIncaricoMostra;

import museo.Museo;

public class IdleStrategy implements Strategy {
    IdleStrategy(){}

    @Override
    public IncaricoMostra strategyMethod(Museo museo) {
        System.out.println("~~~~Idle Strategy");
        return null;
    }
}
