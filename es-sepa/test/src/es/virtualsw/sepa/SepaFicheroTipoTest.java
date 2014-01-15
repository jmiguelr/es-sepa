package src.es.virtualsw.sepa;

import es.virtualsw.sepa.data.SepaFichero;

import java.util.Date;

/**
 * Work from jmiguel@virtualsw.es ( AKA: jmiguel.rodriguel@gmail.com , AKA: me@jmiguel.eu ) and oscar@virtualsw.com
 * <p/>
 * This source code is licensed under Apache 2.0 license schema: you can modify and distribute this piece of software ine
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
