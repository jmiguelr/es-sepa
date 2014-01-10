package es.virtualsw.sepa.data;

/**
 * Created by
 * User: jmiguel
 * Date: 9/01/14
 * Time: 06:22
 */

public interface SepaOperacion {
    String getIdFichero() ;
    String getIdPago() ;
    String getIdOperacion() ;
    Integer getImporte() ;
    String getIdMandato() ;
    String getIdModificacionDeMandato() ;
    String getNombreAnteriorDeAcreedor() ;
    String getIdAnteriorDeAcreedor() ;
    String getIBANAnteriorDeAcreedor() ;
    String getBICDeudor() ;
    String getNombreDeudor() ;
    boolean esDeudorPersonaJuridica() ;
    String getNIFDeudor() ;
    String getSufijoDeudor() ;
    String getCodigoPaisDeudor() ;
    String getIBANCuentaDeudor() ;
    String getConceptoDeOperacion() ;

}
