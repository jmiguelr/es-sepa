package src.es.virtualsw.sepa;

import es.virtualsw.sepa.data.SepaFichero;
import es.virtualsw.sepa.data.SepaPago;
import es.virtualsw.sepa.data.SepaPagoCreator;

import java.util.Vector;

/**
 * Created by
 * User: jmiguel
 * Date: 10/01/14
 * Time: 12:17
 */
public class SepaPagoCreatorTipoTest implements SepaPagoCreator {
    Vector<SepaPago> sepaPagos;


    @Override
    public boolean process(SepaFichero sepaFichero) {

        // TODO: Vamos a BD, o a donde haga falta y creamos el Vector de SepaOperacion

        sepaPagos = new Vector<SepaPago>() ;
        sepaPagos.add( new SepaPagosTipoTest() ) ;

        return true;
    }

    @Override
    public Vector<SepaPago> getSepaPagos() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
