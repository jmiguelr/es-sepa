package es.virtualsw.sepa;

import es.virtualsw.sepa.data.*;
import es.virtualsw.sepa.exceptions.InvalidDataException;
import es.virtualsw.sepa.exceptions.StopProcessingException;
import iso.std.iso._20022.tech.xsd.pain_001_001_03.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

/**
 * Created by oscar on 27/10/15.
 */
public class PagoDirecto {

    private Document document;
    private SepaFicheroCreator sepaFicheroCreator;
    private SepaPagoCreator sepaPagoCreator;
    private SepaOperacionCreator sepaOperacionCreator;
    private static final String DEFAULT_COUNTRY = "ES";

    public PagoDirecto(SepaFicheroCreator _sepaFicheroCreator, SepaPagoCreator _sepaPagoCreator, SepaOperacionCreator _sepaOperacionCreator) {
        sepaFicheroCreator = _sepaFicheroCreator;
        sepaPagoCreator = _sepaPagoCreator;
        sepaOperacionCreator = _sepaOperacionCreator;
    }

    public void generaDocumento() throws StopProcessingException {

        sepaFicheroCreator.process();
        SepaFichero sepaFichero = sepaFicheroCreator.getFichero();

        document = new Document();
        // Initialize message root
        document.setCstmrCdtTrfInitn(new CustomerCreditTransferInitiationV03());

        GroupHeader32 groupHeader = generaGroupHeader(sepaFichero);

        // Add group header to document
        document.getCstmrCdtTrfInitn().setGrpHdr(groupHeader);

        // Comienza el proceso de pagos
        sepaPagoCreator.process(sepaFichero);
        Vector<SepaPago> sepaPagos = sepaPagoCreator.getSepaPagos();

        PaymentInstructionInformation3 paymentInstructionInformation;
        int dummyTxCounter;
        int prevTxCount;
        BigDecimal importeAcumulado;

        for (SepaPago sepaPago : sepaPagos) {
            paymentInstructionInformation = generaSepaPago(sepaFichero, sepaPago);
            document.getCstmrCdtTrfInitn().getPmtInf().add(paymentInstructionInformation);

            // Actualizamos contadores.
            prevTxCount = Integer.parseInt(document.getCstmrCdtTrfInitn().getGrpHdr().getNbOfTxs());
            dummyTxCounter = Integer.parseInt(paymentInstructionInformation.getNbOfTxs());
            document.getCstmrCdtTrfInitn().getGrpHdr().setNbOfTxs(Integer.toString(prevTxCount + dummyTxCounter));

            // Actualizamos importe
            importeAcumulado = document.getCstmrCdtTrfInitn().getGrpHdr().getCtrlSum();
            importeAcumulado = importeAcumulado.add(paymentInstructionInformation.getCtrlSum());
            importeAcumulado = importeAcumulado.setScale(2);
            document.getCstmrCdtTrfInitn().getGrpHdr().setCtrlSum(importeAcumulado);

        }
    }

    private GroupHeader32 generaGroupHeader(SepaFichero sepaFichero) throws StopProcessingException {
        // Create group header
        GroupHeader32 groupHeader = new GroupHeader32();
        groupHeader.setMsgId(sepaFichero.getMsgId());

        // Set date
        groupHeader.setCreDtTm(SepaUtils.ISODateTime(new Date()));

        // Set number of transactions
        groupHeader.setNbOfTxs("0");
        // Set control Sum
        groupHeader.setCtrlSum(BigDecimal.ZERO);        // TODO: Esto hay que reemplazarlo al terminar el documento.


        // InitgPty
        PartyIdentification32 party = new PartyIdentification32();
        party.setNm(sepaFichero.getPresentadorNombre());

        // Id de InitgPty
        Party6Choice party6Choice = new Party6Choice();

        //   <OrgId> O <PrvtId>
        if (sepaFichero.esPresentadorPersonaJuridica()) {
            OrganisationIdentification4 organisationIdentification = new OrganisationIdentification4();
            GenericOrganisationIdentification1 genericOrganisationIdentification1 = new GenericOrganisationIdentification1();

            try {
                genericOrganisationIdentification1.setId(SepaUtils.identificadorUnicoDeInterviniente(sepaFichero));
            } catch (InvalidDataException e) {
                throw new StopProcessingException(e);
            }

            organisationIdentification.getOthr().add(genericOrganisationIdentification1);
            party6Choice.setOrgId(organisationIdentification);

        } else {
            PersonIdentification5 personIdentification5 = new PersonIdentification5();
            GenericPersonIdentification1 genericPersonIdentification1 = new GenericPersonIdentification1();

            try {
                genericPersonIdentification1.setId(SepaUtils.identificadorUnicoDeInterviniente(sepaFichero));
            } catch (InvalidDataException e) {
                throw new StopProcessingException(e);
            }
            personIdentification5.getOthr().add(genericPersonIdentification1);
            party6Choice.setPrvtId(personIdentification5);
        }

        party.setId(party6Choice);
        groupHeader.setInitgPty(party);
        return groupHeader;
    }

    private PaymentInstructionInformation3 generaSepaPago(SepaFichero sepaFichero, SepaPago sepaPago) throws StopProcessingException {
        //INFORMACION DEL PAGO <PmtInf>
        PaymentInstructionInformation3 paymentInstructionInformation = new PaymentInstructionInformation3();
        //<PmtInfId>
        paymentInstructionInformation.setPmtInfId(sepaPago.getIdPago());
        //<PmtMtd>
        paymentInstructionInformation.setPmtMtd(PaymentMethod3Code.TRF);

        paymentInstructionInformation.setNbOfTxs(new BigDecimal("0").toString());
        paymentInstructionInformation.setCtrlSum(new BigDecimal("0"));

        //<ReqdExctnDt>
        paymentInstructionInformation.setReqdExctnDt(SepaUtils.ISODate(sepaPago.getFechaDeCobro()));

        //Ordenante <Dbtr>
        PartyIdentification32 partyIdentification32 = new PartyIdentification32();
        //<Nm>
        partyIdentification32.setNm(sepaPago.getAcreedorNombre());

        //Dirección postal <PstlAdr>
        PostalAddress6 postalAddress = new PostalAddress6();
        //<Ctry>
        postalAddress.setCtry(sepaPago.getAcreedorPais());
        //<AdrLine>
        if(!"".equals(sepaPago.getAcreedorDireccion())) {
            postalAddress.getAdrLine().add(sepaPago.getAcreedorDireccion());
        }
        // CARGAMOS <PstlAdr>
        partyIdentification32.setPstlAdr(postalAddress);

        //Otra <Othr>
        Party6Choice party6Choice = new Party6Choice();

        //Persona jurídica  <OrgId> Ó Persona física <PrvtId>
        if (sepaPago.acreedorEsPersonaJuridica()) {
            OrganisationIdentification4 organisationIdentification = new OrganisationIdentification4();
            GenericOrganisationIdentification1 genericOrganisationIdentification1 = new GenericOrganisationIdentification1();

            try {
                //<Id>
                genericOrganisationIdentification1.setId(SepaUtils.identificadorUnicoDeInterviniente(sepaPago));
            } catch (InvalidDataException e) {
                throw new StopProcessingException(e);
            }
            //CARGAMOS <Othr>
            organisationIdentification.getOthr().add(genericOrganisationIdentification1);
            //CARGAMOS <OrgId>
            party6Choice.setOrgId(organisationIdentification);
        } else {

            PersonIdentification5 personIdentification5 = new PersonIdentification5();
            GenericPersonIdentification1 genericPersonIdentification1 = new GenericPersonIdentification1();

            try {
                //<Id>
                genericPersonIdentification1.setId(SepaUtils.identificadorUnicoDeInterviniente(sepaPago));
            } catch (InvalidDataException e) {
                throw new StopProcessingException(e);
            }
            //CARGAMOS <Othr>
            personIdentification5.getOthr().add(genericPersonIdentification1);
            //CARGAMOS <PrvtId>
            party6Choice.setPrvtId(personIdentification5);
        }
        //CARGAMOS <Id>
        partyIdentification32.setId(party6Choice);
        //CARGAMOS <Dbtr>
        paymentInstructionInformation.setDbtr(partyIdentification32);

        //CUENTA DEL ORDENATE
        CashAccount16 cashAccount16 = new CashAccount16();
        AccountIdentification4Choice accountIdentification4Choice = new AccountIdentification4Choice();
        accountIdentification4Choice.setIBAN(sepaPago.getAcreedorIBAN());
        cashAccount16.setId(accountIdentification4Choice);
        cashAccount16.setCcy("EUR");
        paymentInstructionInformation.setDbtrAcct(cashAccount16);

        //ENTIDAD ORDENANTE
        BranchAndFinancialInstitutionIdentification4 branchAndFinancialInstitutionIdentification4 = new BranchAndFinancialInstitutionIdentification4();
        FinancialInstitutionIdentification7 financialInstitutionIdentification7 = new FinancialInstitutionIdentification7();
        financialInstitutionIdentification7.setBIC(sepaPago.getAcreedorBIC());
        branchAndFinancialInstitutionIdentification4.setFinInstnId(financialInstitutionIdentification7);
        paymentInstructionInformation.setDbtrAgt(branchAndFinancialInstitutionIdentification4);


        //INFORMACIÓN DE TRANSFERENCIA INDIVIDUAL
        sepaOperacionCreator.process(sepaPago);
        Vector<SepaOperacion> sepaOperaciones = sepaOperacionCreator.getSepaOperaciones();

        CreditTransferTransactionInformation10 creditTransferTransactionInformation10;
        int dummyCounter;
        BigDecimal importeAcumulado;
        for (SepaOperacion sepaOperacion : sepaOperaciones) {

            creditTransferTransactionInformation10 = generaSepaOperacion(sepaFichero, sepaPago, sepaOperacion);

            paymentInstructionInformation.getCdtTrfTxInf().add(creditTransferTransactionInformation10);

            // Actualizamos los valores de numeros de transacciones e importe
            dummyCounter = new Integer(paymentInstructionInformation.getNbOfTxs()).intValue();
            paymentInstructionInformation.setNbOfTxs("" + (++dummyCounter));

            importeAcumulado = paymentInstructionInformation.getCtrlSum();
            importeAcumulado = importeAcumulado.add(creditTransferTransactionInformation10.getAmt().getInstdAmt().getValue());
            importeAcumulado = importeAcumulado.setScale(2);
            paymentInstructionInformation.setCtrlSum(importeAcumulado);

        }

        return paymentInstructionInformation;

    }

    private CreditTransferTransactionInformation10 generaSepaOperacion(SepaFichero sepaFichero, SepaPago sepaPago, SepaOperacion sepaOperacion) {
        CreditTransferTransactionInformation10 creditTransferTransactionInformation10 = new CreditTransferTransactionInformation10();

        PaymentInstructionInformation3 paymentInstructionInformation = new PaymentInstructionInformation3();

        //INDENTIFICCION DEL PAGO
        PaymentIdentification1 paymentIdentification1 = new PaymentIdentification1();
        paymentIdentification1.setEndToEndId(sepaOperacion.getIdOperacionExtremo());
        creditTransferTransactionInformation10.setPmtId(paymentIdentification1);

        //INFORMACIÓN DEL TIPO DE PAGO
        PaymentTypeInformation19 paymentTypeInformation19 = new PaymentTypeInformation19();
        ServiceLevel8Choice serviceLevel8Choice = new ServiceLevel8Choice();
        serviceLevel8Choice.setCd("SEPA");
        paymentTypeInformation19.setSvcLvl(serviceLevel8Choice);

        CategoryPurpose1Choice categoryPurpose1Choice = new CategoryPurpose1Choice();
        categoryPurpose1Choice.setCd("CASH");
        paymentTypeInformation19.setCtgyPurp(categoryPurpose1Choice);

        creditTransferTransactionInformation10.setPmtTpInf(paymentTypeInformation19);

        //IMPORTE
        AmountType3Choice amountType3Choice = new AmountType3Choice();
        ActiveOrHistoricCurrencyAndAmount activeOrHistoricCurrencyAndAmount = new ActiveOrHistoricCurrencyAndAmount();
        activeOrHistoricCurrencyAndAmount.setCcy("EUR");
        activeOrHistoricCurrencyAndAmount.setValue(sepaOperacion.getImporte().setScale(2));
        amountType3Choice.setInstdAmt(activeOrHistoricCurrencyAndAmount);

        creditTransferTransactionInformation10.setAmt(amountType3Choice);

        //BENEFICIARIO
        PartyIdentification32 partyIdentification32 = new PartyIdentification32();
        partyIdentification32.setNm(sepaOperacion.getNombreAnteriorDeAcreedor());
        creditTransferTransactionInformation10.setCdtr(partyIdentification32);

        //CUENTA DEL BENEFICIARIO
        CashAccount16 cashAccount16 = new CashAccount16();
        AccountIdentification4Choice accountIdentification4Choice = new AccountIdentification4Choice();
        accountIdentification4Choice.setIBAN(sepaOperacion.getIBANAnteriorDeDeudor());
        cashAccount16.setId(accountIdentification4Choice);

        creditTransferTransactionInformation10.setCdtrAcct(cashAccount16);

        //CONCEPTO
        RemittanceInformation5 remittanceInformation5 = new RemittanceInformation5();
        remittanceInformation5.getUstrd().add(sepaOperacion.getConceptoDeOperacion());
        creditTransferTransactionInformation10.setRmtInf(remittanceInformation5);

        return creditTransferTransactionInformation10;
    }


    public void write(String fileName) throws JAXBException, IOException {
        write(new File(fileName));
    }

    public void write(File outputFile) throws JAXBException, IOException {
        write(outputFile, true);
    }

    public void write(File outputFile, boolean formattedOutput) throws JAXBException, IOException {
        FileWriter file = new FileWriter(outputFile);
        JAXBContext jc = JAXBContext.newInstance(Document.class);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, formattedOutput);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.marshal(new ObjectFactory().createDocument(document), new BufferedWriter(file));
        file.close();
    }

}
