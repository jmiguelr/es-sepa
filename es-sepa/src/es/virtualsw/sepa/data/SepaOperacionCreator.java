package es.virtualsw.sepa.data;

import es.virtualsw.sepa.exceptions.StopProcessingException;

import java.util.Vector;

/**
 * Created by
 * User: jmiguel
 * Date: 9/01/14
 * Time: 22:12
 */
public interface SepaOperacionCreator {

    public void process( SepaPago sepaPago ) throws StopProcessingException;
    Vector<SepaOperacion> getSepaOperaciones() ;
}
