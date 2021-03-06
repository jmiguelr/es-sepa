package es.virtualsw.sepa.data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Work from jmiguel@virtualsw.es ( AKA: jmiguel.rodriguel@gmail.com , AKA: me@jmiguel.eu ) and oscar@virtualsw.com
  *
  * This source code is licensed under Apache 2.0 license schema: you can modify and distribute this piece of software in
  * any way you want. Altought not needed, we'd be glad to see this text included whenever you use this software and a recognition
  * message to know our work has been useful for you. A good place could be the github page https://github.com/jmiguelr/es-sepa
  *
  *
  * Fuentes creados por jmiguel@virtualsw.es ( AKA: jmiguel.rodriguel@gmail.com , AKA: me@jmiguel.eu ) y oscar@virtualsw.com
  * Este codigo fuente se distribuye con licencia Apache 2.0, asi que puede modificarlo o distribuirlo de la forma que quieras.
  * Aunque no se requiere, se agradeceria que incluyeras este texto donde vayas a usar este software y que nos enviaras un
  * mensaje para saber que nuestro trabajo ha sido util para ti. Un buen sitio, puede ser en la pagina del repositorio de GitHub
  * https://github.com/jmiguelr/es-sepa
  *
  * Remotely based on original work from: https://github.com/joaoosorio/pt-sepa-iso20022
  * <p/>
  * <p/>
  * <p/>
 */

public interface SepaOperacion {
    String getIdFichero() ;
    String getIdPago() ;
    String getIdOperacion() ;
    String getIdOperacionExtremo() ;
    BigDecimal getImporte() ;
    String getIdMandato() ;
    Date getFechaDeMandato() ;
    String getIdModificacionDeMandato() ;

    String getNombreAnteriorDeAcreedor() ;
    String getIdAnteriorDeAcreedor() ;
    String getIBANAnteriorDeDeudor() ;

    String getBICDeudor() ;
    String getNombreDeudor() ;
    boolean esDeudorPersonaJuridica() ;
    String getNIFDeudor() ;
    String getSufijoDeudor() ;
    String getCodigoPaisDeudor() ;
    String getIBANCuentaDeudor() ;
    String getConceptoDeOperacion() ;
}
