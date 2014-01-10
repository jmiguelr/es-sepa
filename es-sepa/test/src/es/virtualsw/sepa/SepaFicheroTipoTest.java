package src.es.virtualsw.sepa;

import es.virtualsw.sepa.data.SepaFichero;

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
        return id;
    }

    @Override
    public String msgId() {
        return "XX";
    }

    @Override
    public String fechaDeCreacion() {
        return "XX";
    }

    @Override
    public boolean presentadorEsPersonaJuridica() {
        return false;
    }

    @Override
    public String presentadorNIF() {
        return "XX";
    }

    @Override
    public String presentadorSufijo() {
        return "XX";
    }

    @Override
    public String presentadorPais() {
        return "XX";
    }

    @Override
    public String presentadorNombre() {
        return "XX";
    }
}
