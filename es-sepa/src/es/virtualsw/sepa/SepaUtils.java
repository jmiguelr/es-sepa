package es.virtualsw.sepa;

import es.virtualsw.sepa.data.SepaFichero;
import es.virtualsw.sepa.data.SepaFicheroExtendido;
import es.virtualsw.sepa.exceptions.InvalidDataException;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Original author comment:
 * <p/>
 * Utility class containing various helper methods to generate common attributes
 *
 * @author "Joao Osorio <joao.osorio@sibace.pt>"
 *         <p/>
 *         <p/>
 *         <p/>
 *         <p/>
 *         Work from jmiguel@virtualsw.es ( AKA: jmiguel.rodriguel@gmail.com , AKA: me@jmiguel.eu ) and oscar@virtualsw.com
 *         <p/>
 *         This source code is licensed under Apache 2.0 license schema: you can modify and distribute this piece of software in
 *         any way you want. Altought not needed, we'd be glad to see this text included whenever you use this software and a recognition
 *         message to know our work has been useful for you. A good place could be the github page https://github.com/jmiguelr/es-sepa
 *         <p/>
 *         <p/>
 *         Fuentes creados por jmiguel@virtualsw.es ( AKA: jmiguel.rodriguel@gmail.com , AKA: me@jmiguel.eu ) y oscar@virtualsw.com
 *         Este codigo fuente se distribuye con licencia Apache 2.0, asi que puede modificarlo o distribuirlo de la forma que quieras.
 *         Aunque no se requiere, se agradeceria que incluyeras este texto donde vayas a usar este software y que nos enviaras un
 *         mensaje para saber que nuestro trabajo ha sido util para ti. Un buen sitio, puede ser en la pagina del repositorio de GitHub
 *         https://github.com/jmiguelr/es-sepa
 *         <p/>
 *         Remotely based on original work from: https://github.com/joaoosorio/pt-sepa-iso20022
 *         <p/>
 *         <p/>
 *         <p/>
 */
public class SepaUtils {

    private final static String ADD_TO_DC = "00";
    private final static int RESTA_TO_TABLA_1 = 55;
    private final static int VALUE_DIVISOR_MODELO = 97;
    private final static int VALUE_MODELO = 98;

    /**
     * Converts java.util.Date into XMLGregorianCalendar (required by the xml structure), using the ISO DateTime pattern to represent date and time.
     *
     * @param date
     * @return
     */
    public static XMLGregorianCalendar ISODateTime(Date date) {
        XMLGregorianCalendar dateTime;
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        try {
            dateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
            dateTime.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);
            dateTime.setTimezone(DatatypeConstants.FIELD_UNDEFINED);

        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
        return dateTime;
    }

    /**
     * Converts java.util.Date into XMLGregorianCalendar (required by the xml structure), using the ISO Date pattern to represent date.
     *
     * @param date
     * @return
     */
    public static XMLGregorianCalendar ISODate(Date date) {
        XMLGregorianCalendar dateOnly;
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        try {
            dateOnly = DatatypeFactory.newInstance().newXMLGregorianCalendarDate(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH),
                    DatatypeConstants.FIELD_UNDEFINED);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
        return dateOnly;
    }

    public static String genericId(String ref) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-HHmm");
        return ref + "-" + df.format(new Date());
    }

    public static String paymentid(String paymentGroupId, long seq) {
        DecimalFormat df = new DecimalFormat("00000");
        return paymentGroupId + "-" + df.format(seq);
    }


    // For Portugal:
    public static Map<String, String> NibToBicMapping;

    static {
        Map<String, String> temp = new HashMap<String, String>();

        temp.put("0001", "BGALPTPL");
        temp.put("0007", "BESCPTPL");
        temp.put("0008", "BAIPPTPL");
        temp.put("0010", "BBPIPTPL");
        temp.put("0012", "CDACPTPA");
        temp.put("0014", "IVVSPTPL");
        temp.put("0018", "TOTAPTPL");
        temp.put("0019", "BBVAPTPL");
        temp.put("0022", "BRASPTPL");
        temp.put("0023", "BCOMPTPL");
        temp.put("0025", "CXBIPTPL");
        temp.put("0027", "BPIPPTPL");
        temp.put("0029", "GEBAPTPL");
        temp.put("0032", "BARCPTPL");
        temp.put("0033", "BCOMPTPL");
        temp.put("0034", "BNPAPTPL");
        temp.put("0035", "CGDIPTPL");
        temp.put("0036", "MPIOPTPL");
        temp.put("0038", "BNIFPTPL");
        temp.put("0043", "DEUTPTPL");
        temp.put("0045", "CCCMPTPL");
        temp.put("0046", "CRBNPTPL");
        temp.put("0047", "ESSIPTPL");
        temp.put("0048", "BFIAPTPL");
        temp.put("0049", "INIOPTP1");
        temp.put("0059", "CEMAPTP2");
        temp.put("0061", "BDIGPTPL");
        temp.put("0063", "BNFIPTPL");
        temp.put("0064", "BPGPPTPL");
        temp.put("0065", "BESZPTPL");
        temp.put("0073", "IBNBPTP1");
        temp.put("0076", "FBCOPTPP");
        temp.put("0079", "BPNPPTPL");
        temp.put("0086", "EFISPTPL");
        temp.put("0092", "CAVIPTPP");
        temp.put("0097", "CCCHPTP1");
        temp.put("0098", "CERTPTP1");
        temp.put("0099", "CSSOPTPX");
        temp.put("0160", "BESAPTPA");
        temp.put("0168", "CAHMPTPL");
        temp.put("0169", "CITIPTPX");
        temp.put("0170", "CAGLPTPL");
        temp.put("0183", "PRTTPTP1");
        temp.put("0188", "BCCBPTPL");
        temp.put("0189", "BAPAPTPL");
        temp.put("0235", "CAOEPTP1");
        temp.put("0244", "MPCGPTP1");
        temp.put("0260", "SHHBPTP1");
        temp.put("0484", "ONIFPTP1");
        temp.put("0500", "BBRUPTPL");
        temp.put("0698", "UIFCPTP1");
        temp.put("0781", "IGCPPTPL");
        temp.put("0881", "CRBBPTP1");
        temp.put("0916", "CDCTPTP2");
        temp.put("5180", "CDCTPTP2");
        temp.put("5200", "CTIUPTP1");
        temp.put("5340", "CTLOPTP1");

        NibToBicMapping = Collections.unmodifiableMap(temp);
    }

    public static String bicFromNib(String nib) {
        String bankCode = nib.substring(0, 4);
        if (NibToBicMapping.get(bankCode) == null) {
            System.out.println("Unknown Bank Code: " + bankCode);
        }
        return NibToBicMapping.get(bankCode);
    }

    public static String nibToIban(String nib) {
        return "PT50" + nib;
    }

    public static String identificadorUnicoDeInterviniente(SepaFichero sepaFichero) throws InvalidDataException {
        if (sepaFichero instanceof SepaFicheroExtendido) {
            SepaFicheroExtendido spe = (SepaFicheroExtendido) sepaFichero;
            String idExt = spe.getIdentificadorExtendido();
            if (idExt.equals("")){
                return identificadorUnicoDeInterviniente(sepaFichero.getPresentadorNIF(), sepaFichero.getPresentadorSufijo(), sepaFichero.getPresentadorPais());
            } else {
                return identificadorUnicoDeInterviniente(idExt);
            }
        } else {
            return identificadorUnicoDeInterviniente(sepaFichero.getPresentadorNIF(), sepaFichero.getPresentadorSufijo(), sepaFichero.getPresentadorPais());
        }
    }

    public static String identificadorUnicoDeInterviniente(String identificador) throws InvalidDataException {
        if (identificador.equals("")) {
            throw new InvalidDataException("El IDENTIFICADOR no esta definido");
        }
        if (identificador.length() > 32) {
            throw new InvalidDataException("El IDENTIFICADOR es mayor de 32 caracteres");
        }

        return identificador.toUpperCase();
    }


    public static String identificadorUnicoDeInterviniente(String nif, String codComercial, String codigoDePais) throws InvalidDataException {
        String codigoDeInterviniente = "";

        if (nif == null || codComercial == null || codigoDePais == null) {
            throw new InvalidDataException("Invalid Data: some required parameter is null");
        }

        nif = nif.toUpperCase();
        codComercial = codComercial.toUpperCase();
        codigoDePais = codigoDePais.toUpperCase();

        // System.out.println("Parametros para Generacion:" + NIF + "|" + COD_COMERCIAL + "|" + COD_PAIS + "|");

        // Comprobamos si los datos que tenemos son correctos para generar
        if (nif.equals("")) {
            throw new InvalidDataException("El NIF no esta definido");
        }
        if (codComercial.length() != 3) {
            throw new InvalidDataException("El Codigo Comercial no esta definido o tiene longitud incorrecta");
        }
        if (codigoDePais.length() != 2) {
            throw new InvalidDataException("Codigo de pais incorrecto");
        }


        //Posicion 1 y 2 - Codigo de Pais
        codigoDeInterviniente += codigoDePais;

        //Posicion 3 y 4 Digitos de Control
        nif = limpiarNIFCIF(nif);
        codigoDeInterviniente += generaDigitoControl(nif, codigoDePais);

        //Posicion 5 a 7
        codigoDeInterviniente += codComercial;

        //Posicion 8 a 35
        codigoDeInterviniente += nif;

        return codigoDeInterviniente;
    }

    private static String generaDigitoControl(String nif, String codigoDePais) throws InvalidDataException {
        String numbers = "0123456789";
        String cadena = nif + codigoDePais + ADD_TO_DC;
        String retorno = cadena;
        char caracter;

        // Convertimos letras en numeros
        for (int x = 0; x < cadena.length(); x++) {
            caracter = cadena.charAt(x);
            if (numbers.indexOf(caracter) < 0) {
                retorno = retorno.replaceAll(String.valueOf(caracter), convertToTabla_1(caracter));
            }
        }


        try {
            // ya tenemos la cadena solo con numeros como requiere la especificacion
            // La pasamos a numeros, sacamos el mod(97) y restamos de 98 para obtener el resultado final
            int resto = (int) (Long.parseLong(retorno) % VALUE_DIVISOR_MODELO);

            // Si algun dia el valor anterior se fuera de rango de Long habria que usar BigInteger, algo como:
            // int resto =  new Integer(new BigInteger(retorno).mod(  new BigInteger( "" + VALUE_DIVISOR_MODELO) ).toString()).intValue() ;

            resto = VALUE_MODELO - resto;

            if (resto < 10) {
                retorno = "0" + resto;
            } else {
                retorno = "" + resto;
            }

        } catch (NumberFormatException e) {
            throw new InvalidDataException(e);
        }


        return retorno;
    }

    private static String convertToTabla_1(char caracter) {
        int ascii = (int) caracter - RESTA_TO_TABLA_1;
        return String.valueOf(ascii);

    }

    private static String limpiarNIFCIF(String NIF) {

        Pattern patron = Pattern.compile("[^a-zA-Z0-9]");
        Matcher encaja = patron.matcher(NIF);
        String retorno = encaja.replaceAll("");

        return retorno;
    }

}
