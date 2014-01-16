package src.es.virtualsw.sepa;

import es.virtualsw.sepa.AdeudoDirecto;
import es.virtualsw.sepa.data.SepaFicheroCreator;
import es.virtualsw.sepa.data.SepaOperacionCreator;
import es.virtualsw.sepa.data.SepaPagoCreator;
import iso.std.iso._20022.tech.xsd.pain_008_001_02.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

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
public class testBasico extends junit.framework.TestCase {


    public void testCreateNewDocument_1() throws Exception {
        String idFicheroAExportar = "MyID";

        SepaFicheroCreator sepaFicheroCreator = new SepaFicheroCreatorTipoTest(idFicheroAExportar);
        SepaPagoCreator sepaPagoCreator = new SepaPagoCreatorTipoTest();
        SepaOperacionCreator sepaOperacionCreator = new SepaOperacionCreatorTipoTest();

        creaDoc(sepaFicheroCreator, sepaPagoCreator, sepaOperacionCreator);
    }

    private void creaDoc(SepaFicheroCreator sepaFicheroCreator, SepaPagoCreator sepaPagoCreator, SepaOperacionCreator sepaOperacionCreator) {
        AdeudoDirecto adeudoDirecto = new AdeudoDirecto(sepaFicheroCreator, sepaPagoCreator, sepaOperacionCreator);

        try {
            adeudoDirecto.generaDocumento();
            adeudoDirecto.write("/tmp/1.xml");
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

/*
    public void testReadDocument_1() throws Exception {
        JAXBContext jc = JAXBContext.newInstance(Document.class);
        // Crear desclasificador
        Unmarshaller um = jc.createUnmarshaller();
        // Desclasificar contenido XML del archivo myDoc.xml en la instancia de objetoJava  .
        Document myJAXBObject = (Document) um.unmarshal(new java.io.FileInputStream( "/tmp/1.xml" ));
        System.out.println( myJAXBObject.getCstmrDrctDbtInitn().getGrpHdr().getMsgId() );
    }
*/






}
