package src.es.virtualsw.sepa;

import es.virtualsw.sepa.data.SepaOperacion;
import es.virtualsw.sepa.data.SepaPago;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by
 * User: jmiguel
 * Date: 10/01/14
 * Time: 10:21
 */
public class SepaOperacionTipoTest implements SepaOperacion {
    @Override
    public String getIdFichero() {
        return "idFichero";
    }

    @Override
    public String getIdPago() {
        return "idPago";
    }

    @Override
    public String getIdOperacion() {
        return "idOperacion";
    }

    @Override
    public String getIdOperacionExtremo() {
        return "idOperacionExtremo";
    }

    @Override
    public BigDecimal getImporte() {
        return new BigDecimal("1000.22");
    }

    @Override
    public String getIdMandato() {
        return "idMandato";
    }

    @Override
    public Date getFechaDeMandato() {
        return new Date();
    }

    @Override
    public String getIdModificacionDeMandato() {
        return "idModificacionMandat";
    }

    @Override
    public String getNombreAnteriorDeAcreedor() {
        return "Anterior Acreedor";
    }

    @Override
    public String getIdAnteriorDeAcreedor() {
        return "idAnteriorAcreedor";
    }

    @Override
    public String getIBANAnteriorDeAcreedor() {
        return "IbanAnterior";
    }

    @Override
    public String getBICDeudor() {
        return "BicDeudor";
    }

    @Override
    public String getNombreDeudor() {
        return "Nombre Deudor";
    }

    @Override
    public boolean esDeudorPersonaJuridica() {
        return false;
    }

    @Override
    public String getNIFDeudor() {
        return "123456789A";
    }

    @Override
    public String getSufijoDeudor() {
        return "SUFDeudor";
    }

    @Override
    public String getCodigoPaisDeudor() {
        return "ES";
    }

    @Override
    public String getIBANCuentaDeudor() {
        return "iBandDeudor";
    }

    @Override
    public String getConceptoDeOperacion() {
        return "El Conceto";
    }

}
