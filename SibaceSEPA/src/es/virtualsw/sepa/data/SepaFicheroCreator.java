package es.virtualsw.sepa.data;

/**
 * Created by
 * User: jmiguel
 * Date: 9/01/14
 * Time: 22:12
 */
public interface SepaFicheroCreator {
    public boolean process() ;
    public SepaFichero getFichero() ;
}
