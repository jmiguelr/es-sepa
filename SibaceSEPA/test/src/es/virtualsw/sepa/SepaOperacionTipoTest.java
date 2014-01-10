package src.es.virtualsw.sepa;

import es.virtualsw.sepa.data.SepaOperacion;
import es.virtualsw.sepa.data.SepaPago;

/**
 * Created by
 * User: jmiguel
 * Date: 10/01/14
 * Time: 10:21
 */
public class SepaOperacionTipoTest implements SepaOperacion {
    @Override
    public String getIdFichero() {
        return null;
    }

    @Override
    public String getIdPago() {
        return null;
    }

    @Override
    public String getIdOperacion() {
        return null;
    }

    @Override
    public Integer getImporte() {
        return null;
    }

    @Override
    public String getIdMandato() {
        return null;
    }

    @Override
    public String getIdModificacionDeMandato() {
        return null;
    }

    @Override
    public String getNombreAnteriorDeAcreedor() {
        return null;
    }

    @Override
    public String getIdAnteriorDeAcreedor() {
        return null;
    }

    @Override
    public String getIBANAnteriorDeAcreedor() {
        return null;
    }

    @Override
    public String getBICDeudor() {
        return null;
    }

    @Override
    public String getNombreDeudor() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean esDeudorPersonaJuridica() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getNIFDeudor() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getSufijoDeudor() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getCodigoPaisDeudor() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getIBANCuentaDeudor() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getConceptoDeOperacion() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
