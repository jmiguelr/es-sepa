package es.virtualsw.sepa.data;

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
    public boolean process( SepaFichero sepaFichero ) ;
    Vector<SepaPago> getSepaPagos() ;
}
