package src.es.virtualsw.sepa;

import es.virtualsw.sepa.data.SepaPago;
import iso.std.iso._20022.tech.xsd.pain_008_001_02.SequenceType1Code;

import java.util.Date;

/**
 * Created by
 * User: jmiguel
 * Date: 10/01/14
 * Time: 12:26
 */
public class SepaPagosTipoTest implements SepaPago {
    @Override
    public String getidFichero() {
        return "idFichero";
    }

    @Override
    public String getIdPago() {
        return "idPago";
    }

    @Override
    public boolean esUnUnicoApuntePorElTotal() {
        return false;
    }

    @Override
    public SequenceType1Code getTipoDeSecuencia() {
        return SequenceType1Code.FNAL;
    }

    @Override
    public Date getFechaDeCobro() {
        return new Date();
    }

    @Override
    public boolean acreedorEsPersonaJuridica() {
        return false;
    }

    @Override
    public String getAcreedorNIF() {
        return "AcreedorNif";
    }

    @Override
    public String getAcreedorSufijo() {
        return "SUF";
    }

    @Override
    public String getAcreedorNombre() {
        return "Acreedor Nombre";
    }

    @Override
    public String getAcreedorPais() {
        return "UK";
    }

    @Override
    public String getAcreedorDireccion() {
        return "London Town";
    }

    @Override
    public String getAcreedorIBAN() {
        return "Acreedor Iban";
    }

    @Override
    public String getAcreedorBIC() {
        return "AcreedorBIC";
    }
}
