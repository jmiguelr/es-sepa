package src.es.virtualsw.sepa;

import es.virtualsw.sepa.SepaUtils;
import es.virtualsw.sepa.exceptions.InvalidDataException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


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


public class testGenCodInterviniente extends TestCase {

    public static Test suite() {
        return new TestSuite(testGenCodInterviniente.class);

    }

    public void testGenerarCodInterviniente() throws InvalidDataException {

        String nif = "ZZ999999999";
        String codComercial = "000";
        String codPais = "ES";

        String codInterviniente = "";

        codInterviniente = SepaUtils.identificadorUnicoDeInterviniente(nif, codComercial, codPais);

        System.out.println("CODIGO INTERVINIENTE:" + codInterviniente);

        if (codInterviniente.equals("")) {
            assertTrue(false);
        } else {
            assertTrue(true);
        }
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

}
