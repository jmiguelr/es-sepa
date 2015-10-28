package src.es.virtualsw.sepa;

import es.virtualsw.sepa.AdeudoDirecto;
import es.virtualsw.sepa.PagoDirecto;
import es.virtualsw.sepa.data.SepaFicheroCreator;
import es.virtualsw.sepa.data.SepaOperacionCreator;
import es.virtualsw.sepa.data.SepaPagoCreator;

/**
 * Created by oscar on 27/10/15.
 */
public class testPagoDirecto extends junit.framework.TestCase {


    public void testCreateNewDocument_1() throws Exception {
        String idFicheroAExportar = "MyID";

        SepaFicheroCreator sepaFicheroCreator = new SepaFicheroCreatorTipoTest(idFicheroAExportar);
        SepaPagoCreator sepaPagoCreator = new SepaPagoCreatorTipoTest();
        SepaOperacionCreator sepaOperacionCreator = new SepaOperacionCreatorTipoTest();

        creaDoc(sepaFicheroCreator, sepaPagoCreator, sepaOperacionCreator);
    }

    private void creaDoc(SepaFicheroCreator sepaFicheroCreator, SepaPagoCreator sepaPagoCreator, SepaOperacionCreator sepaOperacionCreator) {
        PagoDirecto pagoDirecto = new PagoDirecto(sepaFicheroCreator, sepaPagoCreator, sepaOperacionCreator);

        try {
            pagoDirecto.generaDocumento();
            pagoDirecto.write("/home/oscar/Escritorio/SEPA/PagoDirecto1.xml");
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
        Document myJAXBObject = (Document) um.unmarshal( new FileReader( "/tmp/1.xml" ));
        System.out.println( myJAXBObject.getCstmrDrctDbtInitn().getGrpHdr().getMsgId() );
    }
*/


}
