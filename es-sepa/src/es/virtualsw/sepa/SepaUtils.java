package es.virtualsw.sepa;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Original author comment:
 *
 * Utility class containing various helper methods to generate common attributes
 * 
 * @author "Joao Osorio <joao.osorio@sibace.pt>"
 *
 */
public class SepaUtils {

    static String NIF = "";
    static String COD_COMERCIAL = "";
    static String COD_PAIS = "ES";

    static String ADD_TO_DC = "00";
    static int RESTA_TO_TABLA_1 = 55;
    static int VALUE_DIVISOR_MODELO = 97;
    static int VALUE_MODELO = 98;

	/**
	 * Converts java.util.Date into XMLGregorianCalendar (required by the xml structure), using the ISO DateTime pattern to represent date and time.
	 * 
	 * @param date
	 * @return
	 */
	public static XMLGregorianCalendar ISODateTime(Date date){
		XMLGregorianCalendar dateTime;
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
	    try {
			dateTime=DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
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
	public static XMLGregorianCalendar ISODate(Date date){
		XMLGregorianCalendar dateOnly;
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
	    try {
			dateOnly=DatatypeFactory.newInstance().newXMLGregorianCalendarDate(calendar.get(Calendar.YEAR), 
																				calendar.get(Calendar.MONTH)+1, 
																				calendar.get(Calendar.DAY_OF_MONTH),
																				DatatypeConstants.FIELD_UNDEFINED);
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e);
		}		
	    return dateOnly;		
	}

	public static String genericId(String ref){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-HHmm");
		return ref+"-"+df.format(new Date());
	}
	
	public static String paymentid(String paymentGroupId, long seq){
		DecimalFormat df = new DecimalFormat("00000");
		return paymentGroupId+"-"+df.format(seq);
	}
	
	
	public static Map<String,String> NibToBicMapping;
	static {
	    Map<String,String> temp = new HashMap<String, String>();
	    
		temp.put("0001","BGALPTPL");
	    temp.put("0007","BESCPTPL");
	    temp.put("0008","BAIPPTPL");
	    temp.put("0010","BBPIPTPL");
	    temp.put("0012","CDACPTPA");
	    temp.put("0014","IVVSPTPL");
	    temp.put("0018","TOTAPTPL");
	    temp.put("0019","BBVAPTPL");
	    temp.put("0022","BRASPTPL");
	    temp.put("0023","BCOMPTPL");
	    temp.put("0025","CXBIPTPL");
	    temp.put("0027","BPIPPTPL");
	    temp.put("0029","GEBAPTPL");
	    temp.put("0032","BARCPTPL");
	    temp.put("0033","BCOMPTPL");
	    temp.put("0034","BNPAPTPL");
	    temp.put("0035","CGDIPTPL");
	    temp.put("0036","MPIOPTPL");
	    temp.put("0038","BNIFPTPL");
	    temp.put("0043","DEUTPTPL");
	    temp.put("0045","CCCMPTPL");
	    temp.put("0046","CRBNPTPL");
	    temp.put("0047","ESSIPTPL");
	    temp.put("0048","BFIAPTPL");
	    temp.put("0049","INIOPTP1");
	    temp.put("0059","CEMAPTP2");
	    temp.put("0061","BDIGPTPL");
	    temp.put("0063","BNFIPTPL");
	    temp.put("0064","BPGPPTPL");
	    temp.put("0065","BESZPTPL");
	    temp.put("0073","IBNBPTP1");
	    temp.put("0076","FBCOPTPP");
	    temp.put("0079","BPNPPTPL");
	    temp.put("0086","EFISPTPL");
	    temp.put("0092","CAVIPTPP");
	    temp.put("0097","CCCHPTP1");
	    temp.put("0098","CERTPTP1");
	    temp.put("0099","CSSOPTPX");
	    temp.put("0160","BESAPTPA");
	    temp.put("0168","CAHMPTPL");
	    temp.put("0169","CITIPTPX");
	    temp.put("0170","CAGLPTPL");
	    temp.put("0183","PRTTPTP1");
	    temp.put("0188","BCCBPTPL");
	    temp.put("0189","BAPAPTPL");
	    temp.put("0235","CAOEPTP1");
	    temp.put("0244","MPCGPTP1");
	    temp.put("0260","SHHBPTP1");
	    temp.put("0484","ONIFPTP1");
	    temp.put("0500","BBRUPTPL");
	    temp.put("0698","UIFCPTP1");
	    temp.put("0781","IGCPPTPL");
	    temp.put("0881","CRBBPTP1");
	    temp.put("0916","CDCTPTP2");
	    temp.put("5180","CDCTPTP2");	    
	    temp.put("5200","CTIUPTP1");
	    temp.put("5340","CTLOPTP1");

	    NibToBicMapping = Collections.unmodifiableMap(temp);
	}
	
	public static String bicFromNib(String nib){
		String bankCode = nib.substring(0,4);
		if (NibToBicMapping.get(bankCode)==null){
			System.out.println("Unknown Bank Code: "+bankCode);
		}
		return NibToBicMapping.get(bankCode);
	}
	
	public static String nibToIban(String nib){
		return "PT50"+nib;
	}


    //GENERADOR CODIGO INTERVINIENTE
    public static String generaCodInterviniente(String nif, String codComercial, String codPais) {
        String retorno = "";
        boolean genera = true;

        NIF = (nif == null ? "" : nif.toUpperCase());
        COD_COMERCIAL = (codComercial == null ? "" : codComercial.toUpperCase());
        COD_PAIS = (codPais == null ? "" : codPais.toUpperCase());

        System.out.println("Parametros para Generacion:" + NIF + "|" + COD_COMERCIAL + "|" + COD_PAIS + "|");

        try {
            //COMPROBAMOS EL NIF DE LA PERSONA
            if (NIF.equals("")) {
                System.out.println("El NIF no esta definido");
                genera = false;
            }
            //COMPROBAMOS EL CÓDIGO COMERCIAL
            if (COD_COMERCIAL.length() != 3) {
                System.out.println("El Código Comercial no esta definido");
                genera = false;
            }

            //COMPROBAMOS EL CÓDIGO DEL PAÍS
            if (COD_PAIS.length() != 2) {
                System.out.println("El Código de País no esta bien definido");
                genera = false;
            }


            if (genera) {
                //Posicion 1 y 2 - Código de Pais
                retorno += COD_PAIS;

                //Posicion 3 y 4 Dígitos de Control
                NIF = limpiar_NIF_CIF();

                retorno += genDigitoControl();

                //Posicion 5 a 7
                retorno += COD_COMERCIAL;

                //Posicion 8 a 35
                retorno += NIF;
            }


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al generar código de Interviniente: " + e);
        }

        return retorno;
    }

    private static String genDigitoControl() {
        String numbers = "0123456789";
        String cadena = NIF + COD_PAIS + ADD_TO_DC;
        String retorno = cadena;
        char caracter;

        //CONVERTIMOS LAS LETRAS EN NÚMEROS
        for (int x = 0; x < cadena.length(); x++) {
            caracter = cadena.charAt(x);
            if (numbers.indexOf(caracter) < 0) {
                retorno = retorno.replaceAll(String.valueOf(caracter), convert_To_Tabla_1(caracter));
            }
        }

        //YA TENEMOS LA CADENA CON SOLO NUMEROS
        //LA TRASFORMAMOS A NUMEROS Y SACAMOS SU MOD 97
        //Y RESTAMOS A 98 EL RESTO DE LA OPERACIÓN
        int resto = (int) (Long.parseLong(retorno) % VALUE_DIVISOR_MODELO);

        resto = VALUE_MODELO - resto;

        if (resto < 10) {
            retorno = "0" + resto;
        } else {
            retorno = String.valueOf(resto);
        }

        return retorno;
    }

    private static String convert_To_Tabla_1(char caracter) {

        int ascii = (int) caracter - RESTA_TO_TABLA_1;

        return String.valueOf(ascii);

    }

    private static String limpiar_NIF_CIF() {

        Pattern patron = Pattern.compile("[^a-zA-Z0-9]");

        Matcher encaja = patron.matcher(NIF);

        String retorno = encaja.replaceAll("");

        System.out.println("NIF:" + retorno);

        return retorno;
    }

}
