package src.es.virtualsw.sepa;

import es.virtualsw.sepa.data.SepaPago;
import es.virtualsw.sepa.data.SepaPagoExtendido;
import iso.std.iso._20022.tech.xsd.pain_008_001_02.SequenceType1Code;

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
public class SepaPagosTipoTest extends SepaPagoExtendido {
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
        return "12345678N";
    }

    @Override
    public String getAcreedorSufijo() {
        return "GHI";
    }

    @Override
    public String getAcreedorNombre() {
        return "Acreedor Nombre";
    }

    @Override
    public String getAcreedorPais() {
        return "ES";
    }

    @Override
    public String getAcreedorDireccion() {
        return "Acreedor Direccion";
    }

    @Override
    public String getAcreedorIBAN() {
        return "AcreedorIBAN";
    }

    @Override
    public String getAcreedorBIC() {
        return "AcreedorBIC";
    }

    @Override
    public String getIdentificadorExtendido() {
        return "INDENTIFICADO_PAGO_EXT";
    }
}
