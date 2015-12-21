package es.virtualsw.sepa.data;

/**
 * Created by oscar on 21/12/15.
 */
public abstract class SepaPagoExtendido implements SepaPago {

    String identificadorExtendido;

    public String getIdentificadorExtendido() {
        return identificadorExtendido;
    }

    public void setIdentificadorExtendido(String identificadorExtendido) {
        this.identificadorExtendido = identificadorExtendido;
    }


}
