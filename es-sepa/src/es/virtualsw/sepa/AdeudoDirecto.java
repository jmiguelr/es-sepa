package es.virtualsw.sepa;

import es.virtualsw.sepa.data.*;
import es.virtualsw.sepa.exceptions.StopProcessingException;
import iso.std.iso._20022.tech.xsd.pain_008_001_02.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

/**
 * Based on original work from: https://github.com/joaoosorio/pt-sepa-iso20022
 *
 *
 *
 * Clase de creacion de un documento SEPA Direct Debit usando la especificacion pain.008.001.02 xml.
 * Adaptado para legislacion española
 * <p/>
 * Clases ISO generadas con xjc compiler y schema pain.008.001.02.xsd disponible en www.iso20022.org
 *
 */

public class AdeudoDirecto {

    private Document document;
    private SepaFicheroCreator sepaFicheroCreator ;
    private SepaPagoCreator sepaPagoCreator;
    private SepaOperacionCreator sepaOperacionCreator ;

    /**
     * Initialize a SEPA Direct Debit Document
     * Sets GroupHeader with supplied arguments.
     */
    public AdeudoDirecto(SepaFicheroCreator _sepaFicheroCreator, SepaPagoCreator _sepaPagoCreator, SepaOperacionCreator _sepaOperacionCreator) {
        sepaFicheroCreator = _sepaFicheroCreator ;
        sepaPagoCreator = _sepaPagoCreator;
        sepaOperacionCreator = _sepaOperacionCreator ;
    }



    public void generaDocumento() throws StopProcessingException{

        sepaFicheroCreator.process();
        SepaFichero sepaFichero =  sepaFicheroCreator.getFichero() ;


        document = new Document();
        // Initialize message root
        document.setCstmrDrctDbtInitn(new CustomerDirectDebitInitiationV02());

        // Create group header
        GroupHeader39 groupHeader = new GroupHeader39();
        groupHeader.setMsgId(sepaFichero.getMsgId());

        // Set date
        groupHeader.setCreDtTm(SepaUtils.ISODateTime(new Date()));

        // Set number of transactions
        groupHeader.setNbOfTxs("0");
        // Set control Sum
        groupHeader.setCtrlSum(BigDecimal.ZERO);        // TODO: Esto hay que reemplazarlo al terminar el documento.

        PartyIdentification32 party = new PartyIdentification32();
        party.setNm(sepaFichero.getPresentadorNombre());


        Party6Choice tipoDePresentador = new Party6Choice() ;

        if( sepaFichero.esPresentadorPersonaJuridica()) {
            OrganisationIdentification4 organisationIdentification =  new OrganisationIdentification4() ;
            // organisationIdentification.getOthr().add()
            tipoDePresentador.setOrgId( organisationIdentification );
        } else {
            PersonIdentification5 personIdentification5 = new PersonIdentification5() ;
            // personIdentification5.getOthr().add()
            tipoDePresentador.setPrvtId(personIdentification5);
        }


        party.setId( new Party6Choice());
        groupHeader.setInitgPty(party);

        // Add group header to document
        document.getCstmrDrctDbtInitn().setGrpHdr(groupHeader);


        // Comienza el proceso de pagos
        sepaPagoCreator.process( sepaFichero ) ;
        Vector<SepaPago> sepaPagos = sepaPagoCreator.getSepaPagos() ;
        for( SepaPago sepaPago : sepaPagos   ) {
            addSepaPago( sepaFichero ,  sepaPago ) ;

        }

        // TODO: Actualizar el numero de transacciones, CheckSums...


    }

    private void addSepaPago(SepaFichero sepaFichero, SepaPago sepaPago) throws StopProcessingException {

        sepaOperacionCreator.process( sepaPago ) ;
        Vector<SepaOperacion> sepaOperaciones = sepaOperacionCreator.getSepaOperaciones() ;

        for(SepaOperacion sepaOperacion: sepaOperaciones) {
            addSepaOperacion( sepaFichero ,  sepaPago , sepaOperacion  ) ;
        }


    }

    private void addSepaOperacion(SepaFichero sepaFichero, SepaPago sepaPago, SepaOperacion sepaOperacion) throws StopProcessingException{


    }


    public void addPaymentGroup(DirectDebitPaymentGroup paymentGroup) {
        //TODO update control sum e numTransactions;
        document.getCstmrDrctDbtInitn().getPmtInf().add(paymentGroup.getInformation());
    }





    /**
     * @throws java.io.IOException
     */
    public void write(String fileName) throws JAXBException, IOException {
        FileWriter file = new FileWriter(fileName);
        JAXBContext jc = JAXBContext.newInstance(Document.class);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.marshal(new ObjectFactory().createDocument(document), new BufferedWriter(file));
        file.close();
    }

}
