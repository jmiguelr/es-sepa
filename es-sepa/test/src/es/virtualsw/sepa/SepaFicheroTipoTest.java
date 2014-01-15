package src.es.virtualsw.sepa;

import es.virtualsw.sepa.data.SepaFichero;

import java.util.Date;

/**
 * Created by
 * User: jmiguel
 * Date: 10/01/14
 * Time: 06:58
 */


public class SepaFicheroTipoTest implements SepaFichero {
    String id;
    public SepaFicheroTipoTest(String id) {
        this.id = id;
    }

    @Override
    public String getIdFichero() {
        return "idFichero";
    }

    @Override
    public String getMsgId() {
        return "_msgId_";
    }

    @Override
    public Date getFechaDeCreacion() {
        return new Date();
    }

    @Override
    public boolean esPresentadorPersonaJuridica() {
        return false;
    }

    @Override
    public String getPresentadorNIF() {
        return "12345678N";
    }

    @Override
    public String getPresentadorSufijo() {
        return "ABC";
    }

    @Override
    public String getPresentadorPais() {
        return "ES";
    }

    @Override
    public String getPresentadorNombre() {
        return "Jose Miguel";
    }
}
