package es.virtualsw.sepa.data;

import iso.std.iso._20022.tech.xsd.pain_008_001_02.SequenceType1Code;

import java.util.Date;

/**
 * Created by
 * User: jmiguel
 * Date: 9/01/14
 * Time: 22:39
 */
public interface SepaPago {
    String getidFichero();
    String getIdPago();
    boolean esUnUnicoApuntePorElTotal();
    SequenceType1Code getTipoDeSecuencia();
    Date getFechaDeCobro();
    boolean acreedorEsPersonaJuridica();
    String getAcreedorNIF();
    String getAcreedorSufijo();
    String getAcreedorNombre();
    String getAcreedorPais();
    String getAcreedorDireccion();
    String getAcreedorIBAN();
    String getAcreedorBIC();



}
