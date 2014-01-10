package src.es.virtualsw.sepa;

import es.virtualsw.sepa.data.*;

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

    }








    //
    // _______________________________________________________________________________________________________

    private class SepaPagosTest {
        private Vector<SepaPago> sepaPagos;
        private SepaFichero sepaFichero;

        public SepaPagosTest(SepaFichero _sepaFichero) {
            sepaFichero = _sepaFichero;
        }

        public Vector<SepaPago> getSepaPagos() {
            return sepaPagos;
        }
    }

    //
    // _______________________________________________________________________________________________________

    private class SepaOperacionesTest {
        private Vector<SepaOperacion> sepaPago;

        public SepaOperacionesTest(SepaPago sepaPago) {
        }

        public Vector<SepaOperacion> getSepaPago() {
            return sepaPago;
        }
    }
}
