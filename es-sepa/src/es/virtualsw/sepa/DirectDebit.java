package es.virtualsw.sepa;

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
 * Original author comment:
 *
 * Helper class to build and save a SEPA Direct Debit document, using the pain.008.001.02 xml file format, with conditionals specified for operations in Portugal.
 * <p/>
 * Iso classes were created using the xjc compiler and the pain.008.001.02.xsd schema available at the ISO 20022 web site (www.iso20022.org)
 *
 * @author "Joao Osorio <joao.osorio@sibace.pt>"
 */

public class DirectDebit {

    private Document document;

    /**
     * Initialize a SEPA Direct Debit Document
     * Sets GroupHeader with supplied arguments.
     */
    public DirectDebit(String msgId, String companyName) {
        // Initialize document
        document = new Document();

        // Initialize message root
        document.setCstmrDrctDbtInitn(new CustomerDirectDebitInitiationV02());

        // Create group header
        GroupHeader39 groupHeader = new GroupHeader39();

        // Set message id
        groupHeader.setMsgId(msgId);

        // Set date
        SepaUtils.ISODateTime(new Date());

        // Set number of transactions
        groupHeader.setNbOfTxs("0");

        // Set control Sum
        groupHeader.setCtrlSum(BigDecimal.ZERO);

        // Set party identification - based on name only
        // TODO Add id based party identification
        PartyIdentification32 party = new PartyIdentification32();
        party.setNm(companyName);
        groupHeader.setInitgPty(party);

        // Add group header to document
        document.getCstmrDrctDbtInitn().setGrpHdr(groupHeader);
    }

    public void addPaymentGroup(DirectDebitPaymentGroup paymentGroup) {
        //TODO update control sum e numTransactions;
        document.getCstmrDrctDbtInitn().getPmtInf().add(paymentGroup.getInformation());
    }

    /**
     * @throws IOException
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
