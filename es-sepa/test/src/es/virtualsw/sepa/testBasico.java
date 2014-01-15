package src.es.virtualsw.sepa;

import es.virtualsw.sepa.AdeudoDirecto;
import es.virtualsw.sepa.data.*;
import es.virtualsw.sepa.exceptions.StopProcessingException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Vector;

/**
 * Created by
 * User: jmiguel
 * Date: 9/01/14
 * Time: 22:58
 */
public class testBasico extends junit.framework.TestCase {


    public void testBasico1() throws Exception {
        String idFicheroAExportar = "MyID" ;

        SepaFicheroCreator sepaFicheroCreator = new SepaFicheroCreatorTipoTest( idFicheroAExportar);
        SepaPagoCreator sepaPagoCreator = new SepaPagoCreatorTipoTest();
        SepaOperacionCreator sepaOperacionCreator = new SepaOperacionCreatorTipoTest();


        creaDoc(sepaFicheroCreator, sepaPagoCreator, sepaOperacionCreator);


    }

    private void creaDoc(SepaFicheroCreator sepaFicheroCreator, SepaPagoCreator sepaPagoCreator, SepaOperacionCreator sepaOperacionCreator) {
        AdeudoDirecto adeudoDirecto = new AdeudoDirecto(sepaFicheroCreator, sepaPagoCreator, sepaOperacionCreator) ;

        try {
            adeudoDirecto.generaDocumento() ;
            adeudoDirecto.write("/tmp/1.xml");
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

}
