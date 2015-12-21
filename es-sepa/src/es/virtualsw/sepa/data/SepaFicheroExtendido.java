package es.virtualsw.sepa.data;

/**
 * Created by oscar on 18/12/15.
 */
public abstract class SepaFicheroExtendido implements SepaFichero {

    String identificadorExtendido;

    public String getIdentificadorExtendido() {
        return identificadorExtendido;
    }

    public void setIdentificadorExtendido(String identificadorExtendido) {
        this.identificadorExtendido = identificadorExtendido;
    }


}
