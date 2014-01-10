package es.virtualsw.sepa.data;

/**
 * Created by
 * User: jmiguel
 * Date: 9/01/14
 * Time: 22:15
 */
public interface SepaFichero {
    String getIdFichero() ;
    String msgId() ;
    String fechaDeCreacion() ;
    boolean presentadorEsPersonaJuridica() ;
    String presentadorNIF() ;
    String presentadorSufijo() ;
    String presentadorPais() ;
    String presentadorNombre() ;
}
