package es.virtualsw.sepa.data;

import java.util.Vector;

/**
 * Created by
 * User: jmiguel
 * Date: 9/01/14
 * Time: 22:12
 */
public interface SepaOperacionCreator {
    public boolean process( SepaPago sepaPago ) ;
    Vector<SepaOperacion> getSepaOperaciones() ;
}
