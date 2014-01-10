package src.es.virtualsw.sepa;

import es.virtualsw.sepa.data.SepaPago;

/**
 * Created by
 * User: jmiguel
 * Date: 10/01/14
 * Time: 12:26
 */
public class SepaPagosTipoTest implements SepaPago {
    @Override
    public String getidFichero() {
        return null;
    }

    @Override
    public String getIdPago() {
        return null;
    }

    @Override
    public boolean isUnUnicoApuntePorElTotal() {
        return false;
    }

    @Override
    public String getTipoDeSecuencia() {
        return null;
    }

    @Override
    public String getFechaDeCobro() {
        return null;
    }

    @Override
    public boolean acreedorEsPersonaJuridica() {
        return false;
    }

    @Override
    public String getAcreedorNIF() {
        return null;
    }

    @Override
    public String getAcreedorSufijo() {
        return null;
    }

    @Override
    public String getAcreedorNombre() {
        return null;
    }

    @Override
    public String getAcreedorPais() {
        return null;
    }

    @Override
    public String getAcreedorDireccion() {
        return null;
    }

    @Override
    public String getAcreedorIBAN() {
        return null;
    }

    @Override
    public String getAcreedorBIC() {
        return null;
    }
}
