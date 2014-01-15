package src.es.virtualsw.sepa;

import es.virtualsw.sepa.data.SepaFichero;
import es.virtualsw.sepa.data.SepaPago;
import es.virtualsw.sepa.data.SepaPagoCreator;
import es.virtualsw.sepa.exceptions.StopProcessingException;

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
    public void process(SepaFichero sepaFichero) throws StopProcessingException {
        // TODO: Vamos a BD, o a donde haga falta y creamos el Vector de SepaOperacion
        sepaPagos = new Vector<SepaPago>() ;
        sepaPagos.add( new SepaPagosTipoTest() ) ;
        sepaPagos.add( new SepaPagosTipoTest() ) ;
    }

    @Override
    public Vector<SepaPago> getSepaPagos() {
        return sepaPagos ;
    }
}
