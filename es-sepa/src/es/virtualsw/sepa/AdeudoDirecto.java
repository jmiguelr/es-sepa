package es.virtualsw.sepa;

import es.virtualsw.sepa.data.SepaFicheroCreator;
import es.virtualsw.sepa.data.SepaOperacionCreator;
import es.virtualsw.sepa.data.SepaPagoCreator;
import iso.std.iso._20022.tech.xsd.pain_008_001_02.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

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



    public void generaDocumento() {



        document = new Document();

        // Initialize message root
        document.setCstmrDrctDbtInitn(new CustomerDirectDebitInitiationV02());

        // Create group header
        GroupHeader39 groupHeader = new GroupHeader39();
        groupHeader.setMsgId(sepaFicheroCreator.getFichero().getMsgId());

        // Set date
        SepaUtils.ISODateTime(new Date());
        // Set number of transactions
        groupHeader.setNbOfTxs("0");
        // Set control Sum
        groupHeader.setCtrlSum(BigDecimal.ZERO);
        PartyIdentification32 party = new PartyIdentification32();
        party.setNm(sepaFicheroCreator.getFichero().getPresentadorNombre());
        groupHeader.setInitgPty(party);

        // Add group header to document
        document.getCstmrDrctDbtInitn().setGrpHdr(groupHeader);

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
