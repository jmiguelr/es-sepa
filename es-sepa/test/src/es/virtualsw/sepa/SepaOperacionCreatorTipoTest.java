package src.es.virtualsw.sepa;

import es.virtualsw.sepa.data.SepaOperacion;
import es.virtualsw.sepa.data.SepaOperacionCreator;
import es.virtualsw.sepa.data.SepaPago;

import java.util.Vector;

/**
 * Created by
 * User: jmiguel
 * Date: 10/01/14
 * Time: 07:09
 */
public class SepaOperacionCreatorTipoTest implements SepaOperacionCreator {
    Vector<SepaOperacion> sepaOperaciones ;

    @Override
    public boolean process(SepaPago _sepaPago) {
    // TODO: Vamos a BD, o a donde haga falta y creamos el Vector de SepaOperacion

        sepaOperaciones = new Vector<SepaOperacion>() ;
        sepaOperaciones.add( new SepaOperacionTipoTest() ) ;

        return true;
    }

    @Override
    public Vector<SepaOperacion> getSepaOperaciones() {
        return sepaOperaciones ;
    }
}
