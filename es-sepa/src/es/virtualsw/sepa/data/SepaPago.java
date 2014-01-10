package es.virtualsw.sepa.data;

/**
 * Created by
 * User: jmiguel
 * Date: 9/01/14
 * Time: 22:39
 */
public interface SepaPago {
      String getidFichero() ;
      String getIdPago() ;
      boolean isUnUnicoApuntePorElTotal() ;
      String getTipoDeSecuencia() ;
      String getFechaDeCobro() ;
      boolean acreedorEsPersonaJuridica() ;
      String getAcreedorNIF() ;
      String getAcreedorSufijo() ;
      String getAcreedorNombre();
      String getAcreedorPais() ;
      String getAcreedorDireccion() ;
      String getAcreedorIBAN() ;
      String getAcreedorBIC() ;

}
