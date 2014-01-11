package es.virtualsw.sepa.data;

/**
 * Created by
 * User: jmiguel
 * Date: 9/01/14
 * Time: 22:15
 */
public interface SepaFichero {
    String getIdFichero() ;
    String getMsgId() ;
    String getFechaDeCreacion() ;
    boolean esPresentadorPersonaJuridica() ;
    String getPresentadorNIF() ;
    String getPresentadorSufijo() ;
    String getPresentadorPais() ;
    String getPresentadorNombre() ;
}
