package src.es.virtualsw.sepa;

import es.virtualsw.sepa.data.SepaOperacion;
import es.virtualsw.sepa.data.SepaOperacionCreator;
import es.virtualsw.sepa.data.SepaPago;
import es.virtualsw.sepa.exceptions.StopProcessingException;

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
    public void process(SepaPago _sepaPago) throws StopProcessingException {
    // TODO: Vamos a BD, o a donde haga falta y creamos el Vector de SepaOperacion

        sepaOperaciones = new Vector<SepaOperacion>() ;
        sepaOperaciones.add( new SepaOperacionTipoTest() ) ;
        sepaOperaciones.add( new SepaOperacionTipoTest() ) ;


    }

    @Override
    public Vector<SepaOperacion> getSepaOperaciones() {
        return sepaOperaciones ;
    }
}
