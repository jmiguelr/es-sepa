package src.es.virtualsw.sepa;

import es.virtualsw.sepa.data.SepaFichero;
import es.virtualsw.sepa.data.SepaFicheroCreator;

/**
 * Created by
 * User: jmiguel
 * Date: 10/01/14
 * Time: 07:04
 */
public class SepaFicheroCreatorTipoTest implements SepaFicheroCreator {
    SepaFichero sepaFichero ;
    String idFicheroAImportar ;

    public  SepaFicheroCreatorTipoTest(String idFicheroAImportar) {
        this.idFicheroAImportar = idFicheroAImportar;
    }


    @Override
    public boolean process() {

        // TODO: Vamos a BD, o a donde haga falta y creamos el Vector de SepaOperacion

        sepaFichero = new SepaFicheroTipoTest("MiId") ;
        return true ;
    }

    @Override
    public SepaFichero getFichero() {
        return sepaFichero;
    }


}
