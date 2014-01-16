package src.es.virtualsw.sepa;

import es.virtualsw.sepa.data.SepaOperacion;
import es.virtualsw.sepa.data.SepaOperacionCreator;
import es.virtualsw.sepa.data.SepaPago;
import es.virtualsw.sepa.exceptions.StopProcessingException;

import java.util.Vector;

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
public class SepaOperacionCreatorTipoTest implements SepaOperacionCreator {
    Vector<SepaOperacion> sepaOperaciones;

    @Override
    public void process(SepaPago _sepaPago) throws StopProcessingException {
        // TODO: Vamos a BD, o a donde haga falta y creamos el Vector de SepaOperacion

        sepaOperaciones = new Vector<SepaOperacion>();
        sepaOperaciones.add(new SepaOperacionTipoTest());
        sepaOperaciones.add(new SepaOperacionTipoTest());


    }

    @Override
    public Vector<SepaOperacion> getSepaOperaciones() {
        return sepaOperaciones;
    }
}
