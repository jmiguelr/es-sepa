package src.es.virtualsw.sepa;

import es.virtualsw.sepa.data.SepaOperacion;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Work from jmiguel@virtualsw.es ( AKA: jmiguel.rodriguel@gmail.com , AKA: me@jmiguel.eu ) and oscar@virtualsw.com
 * <p/>
 * This source code is licensed under Apache 2.0 license schema: you can modify and distribute this piece of software in
 * any way you want. Altought not needed, we'd be glad to see this text included whenever you use this software and a recognition
 * message to know our work has been useful for you. A good place could be the github page https://github.com/jmiguelr/es-sepa
 * <p/>
 * <p/>
 * Fuentes creados por jmiguel@virtualsw.es ( AKA: jmiguel.rodriguel@gmail.com , AKA: me@jmiguel.eu ) y oscar@virtualsw.com
 * Este codigo fuente se distribuye con licencia Apache 2.0, asi que puede modificarlo o distribuirlo de la forma que quieras.
 * Aunque no se requiere, se agradeceria que incluyeras este texto donde vayas a usar este software y que nos enviaras un
 * mensaje para saber que nuestro trabajo ha sido util para ti. Un buen sitio, puede ser en la pagina del repositorio de GitHub
 * https://github.com/jmiguelr/es-sepa
 * <p/>
 * Remotely based on original work from: https://github.com/joaoosorio/pt-sepa-iso20022
 * <p/>
 * <p/>
 * <p/>
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
        return new BigDecimal("2.10");
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
        return "idModificacionMandato";
    }

    @Override
    public String getNombreAnteriorDeAcreedor() {
        return "Nombra Anterior Acreedor";
    }

    @Override
    public String getIdAnteriorDeAcreedor() {
        return "idAnteriorAcreedor";
    }

    @Override
    public String getIBANAnteriorDeDeudor() {
        //return "IbanAnteriorAcreedor";
        return "";
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
        return "12345678N";
    }

    @Override
    public String getSufijoDeudor() {
        return "DEF";
    }

    @Override
    public String getCodigoPaisDeudor() {
        return "ES";
    }

    @Override
    public String getIBANCuentaDeudor() {
        return "iBanCuentaDeudor";
    }

    @Override
    public String getConceptoDeOperacion() {
        return "Concepto de Operacion";
    }

    @Override
    public String getNombreBeneficiario() {
        return "Nombre Beneficiario";
    }
    public String getIBANBeneficiario() {
        return "IBAN Beneficiario";
    }
}
