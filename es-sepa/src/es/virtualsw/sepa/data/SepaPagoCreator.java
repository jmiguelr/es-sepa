package es.virtualsw.sepa.data;

import es.virtualsw.sepa.exceptions.StopProcessingException;

import java.util.Vector;

/**
 * Created by
 * User: jmiguel
 * Date: 9/01/14
 * Time: 22:12
 *
 * Interface para la generacion de SepaPagos
 * El metodo p
 *
 *
 */
public interface SepaPagoCreator {
    public void process( SepaFichero sepaFichero ) throws StopProcessingException;
    Vector<SepaPago> getSepaPagos() ;
}
