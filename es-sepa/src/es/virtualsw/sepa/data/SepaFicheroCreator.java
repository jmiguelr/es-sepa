package es.virtualsw.sepa.data;

import es.virtualsw.sepa.exceptions.StopProcessingException;

/**
 * Created by
 * User: jmiguel
 * Date: 9/01/14
 * Time: 22:12
 */
public interface SepaFicheroCreator {
    public void process() throws StopProcessingException;
    public SepaFichero getFichero() ;
}
