package es.virtualsw.sepa;

import es.virtualsw.sepa.data.*;
import es.virtualsw.sepa.exceptions.InvalidDataException;
import es.virtualsw.sepa.exceptions.StopProcessingException;
import iso.std.iso._20022.tech.xsd.pain_008_001_02.*;

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
 * Work from jmiguel@virtualsw.es ( AKA: jmiguel.rodriguel@gmail.com , AKA: me@jmiguel.eu ) and oscar@virtualsw.com
 * <p/>
 * This source code is licensed under Apache 2.0 license schema: you can modify and distribute this piece of software in
 * any way you want. Altought not needed, we'd be glad to see this text included whenever you use this software and a recognition
 * message to know our work has been useful for you. A good place could be the github page https://github.com/jmiguelr/es-sepa
 * <p/>
 * <p/>
 * Fuentes creados por jmiguel@virtualsw.es ( AKA: jmiguel.rodriguel@gmail.com , AKA: me@jmiguel.eu ) y oscar@virtualsw.com
 * Este codigo fuente se distribuye con licencia Apache 2.0, asi que puede modificarlo o distribuirlo de la forma que quieras.
 * Aunque no se requiere, se agradeceria que incluyeras este texto donde vayas a usar este software y que nos enviaras un
 * mensaje para saber que nuestro trabajo ha sido util para ti. Un buen sitio, puede ser en la pagina del repositorio de GitHub
 * https://github.com/jmiguelr/es-sepa
 * <p/>
 * Remotely based on original work from: https://github.com/joaoosorio/pt-sepa-iso20022
 * <p/>
 * <p/>
 * <p/>
 * Clase de creacion de un documento SEPA Direct Debit usando la especificacion pain.008.001.02 xml.
 * Adaptado para legislacion española
 * <p/>
 * Clases ISO generadas con xjc compiler y schema pain.008.001.02.xsd disponible en www.iso20022.org
 */

public class AdeudoDirecto {

    private Document document;
    private SepaFicheroCreator sepaFicheroCreator;
    private SepaPagoCreator sepaPagoCreator;
    private SepaOperacionCreator sepaOperacionCreator;
    private static final String DEFAULT_COUNTRY = "ES";


    /**
     * Initialize a SEPA Direct Debit Document
     * Sets GroupHeader with supplied arguments.
     */
    public AdeudoDirecto(SepaFicheroCreator _sepaFicheroCreator, SepaPagoCreator _sepaPagoCreator, SepaOperacionCreator _sepaOperacionCreator) {
        sepaFicheroCreator = _sepaFicheroCreator;
        sepaPagoCreator = _sepaPagoCreator;
        sepaOperacionCreator = _sepaOperacionCreator;
    }


    public void generaDocumento() throws StopProcessingException {

        sepaFicheroCreator.process();
        SepaFichero sepaFichero = sepaFicheroCreator.getFichero();


        document = new Document();
        // Initialize message root
        document.setCstmrDrctDbtInitn(new CustomerDirectDebitInitiationV02());

        GroupHeader39 groupHeader = generaGroupHeader(sepaFichero);

        // Add group header to document
        document.getCstmrDrctDbtInitn().setGrpHdr(groupHeader);


        // Comienza el proceso de pagos
        sepaPagoCreator.process(sepaFichero);
        Vector<SepaPago> sepaPagos = sepaPagoCreator.getSepaPagos();

        PaymentInstructionInformation4 paymentInstructionInformation;
        int dummyTxCounter;
        int prevTxCount;
        BigDecimal importeAcumulado ;

        for (SepaPago sepaPago : sepaPagos) {
            paymentInstructionInformation = generaSepaPago(sepaFichero, sepaPago);
            document.getCstmrDrctDbtInitn().getPmtInf().add(paymentInstructionInformation);


            // Actualizamos contadores.
            prevTxCount = Integer.parseInt(document.getCstmrDrctDbtInitn().getGrpHdr().getNbOfTxs());
            dummyTxCounter = Integer.parseInt(paymentInstructionInformation.getNbOfTxs());
            document.getCstmrDrctDbtInitn().getGrpHdr().setNbOfTxs(Integer.toString(prevTxCount + dummyTxCounter)) ;

            // Actualizamos importe
            importeAcumulado = document.getCstmrDrctDbtInitn().getGrpHdr().getCtrlSum() ;
            importeAcumulado = importeAcumulado.add(paymentInstructionInformation.getCtrlSum()) ;
            importeAcumulado = importeAcumulado.setScale(2) ;
            document.getCstmrDrctDbtInitn().getGrpHdr().setCtrlSum( importeAcumulado ) ;

        }


    }


    private GroupHeader39 generaGroupHeader(SepaFichero sepaFichero) throws StopProcessingException {
        // Create group header
        GroupHeader39 groupHeader = new GroupHeader39();
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

            OrganisationIdentificationSchemeName1Choice organisationIdentificationSchemeName1Choice = new OrganisationIdentificationSchemeName1Choice();
            organisationIdentificationSchemeName1Choice.setCd("CORE");
            GenericOrganisationIdentification1 genericOrganisationIdentification1 = new GenericOrganisationIdentification1();
            genericOrganisationIdentification1.setSchmeNm(organisationIdentificationSchemeName1Choice);

            try {
                genericOrganisationIdentification1.setId(SepaUtils.identificadorUnicoDeInterviniente(sepaFichero.getPresentadorNIF(), sepaFichero.getPresentadorSufijo(), DEFAULT_COUNTRY));
            } catch (InvalidDataException e) {
                throw new StopProcessingException(e);
            }

            organisationIdentification.getOthr().add(genericOrganisationIdentification1);
            party6Choice.setOrgId(organisationIdentification);

        } else {
            PersonIdentification5 personIdentification5 = new PersonIdentification5();
            PersonIdentificationSchemeName1Choice personIdentificationSchemeName1Choice = new PersonIdentificationSchemeName1Choice();
            personIdentificationSchemeName1Choice.setCd("CORE");
            GenericPersonIdentification1 genericPersonIdentification1 = new GenericPersonIdentification1();

            genericPersonIdentification1.setSchmeNm(personIdentificationSchemeName1Choice);
            try {
                genericPersonIdentification1.setId(SepaUtils.identificadorUnicoDeInterviniente(sepaFichero.getPresentadorNIF(), sepaFichero.getPresentadorSufijo(), DEFAULT_COUNTRY));
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


    private PaymentInstructionInformation4 generaSepaPago(SepaFichero sepaFichero, SepaPago sepaPago) throws StopProcessingException {


        PaymentInstructionInformation4 paymentInstructionInformation = new PaymentInstructionInformation4();
        paymentInstructionInformation.setPmtInfId(sepaPago.getIdPago());


        paymentInstructionInformation.setPmtMtd(PaymentMethod2Code.DD);
        paymentInstructionInformation.setBtchBookg(sepaPago.esUnUnicoApuntePorElTotal());
        paymentInstructionInformation.setNbOfTxs(new BigDecimal("0").toString());
        paymentInstructionInformation.setCtrlSum(new BigDecimal("0"));


        // Comienzo de etiqueta PmtTpInf

        PaymentTypeInformation20 paymentTypeInformation20 = new PaymentTypeInformation20();
        ServiceLevel8Choice serviceLevel8Choice = new ServiceLevel8Choice();
        serviceLevel8Choice.setCd("SEPA");
        paymentTypeInformation20.setSvcLvl(serviceLevel8Choice);
        paymentInstructionInformation.setPmtTpInf(paymentTypeInformation20);


        paymentTypeInformation20.setLclInstrm(new LocalInstrument2Choice());
        paymentTypeInformation20.getLclInstrm().setCd("CORE");

        paymentTypeInformation20.setSeqTp(sepaPago.getTipoDeSecuencia());

        paymentInstructionInformation.setReqdColltnDt(SepaUtils.ISODate(sepaPago.getFechaDeCobro()));

        PartyIdentification32 partyIdentification32 = new PartyIdentification32();
        partyIdentification32.setNm(sepaPago.getAcreedorNombre());
        PostalAddress6 postalAddress = new PostalAddress6();
        postalAddress.setCtry(sepaPago.getAcreedorPais());
        postalAddress.getAdrLine().add(sepaPago.getAcreedorDireccion());
        partyIdentification32.setPstlAdr(postalAddress);
        paymentInstructionInformation.setCdtr(partyIdentification32);


        CashAccount16 cashAccount = new CashAccount16();
        AccountIdentification4Choice accountIdentification4Choice = new AccountIdentification4Choice();
        accountIdentification4Choice.setIBAN(sepaPago.getAcreedorIBAN());
        cashAccount.setId(accountIdentification4Choice);
        paymentInstructionInformation.setCdtrAcct(cashAccount);

        BranchAndFinancialInstitutionIdentification4 branchAndFinancialInstitutionIdentification = new BranchAndFinancialInstitutionIdentification4();
        FinancialInstitutionIdentification7 financialInstitutionIdentification7 = new FinancialInstitutionIdentification7();
        financialInstitutionIdentification7.setBIC(sepaPago.getAcreedorBIC());

        branchAndFinancialInstitutionIdentification.setFinInstnId(financialInstitutionIdentification7);
        paymentInstructionInformation.setCdtrAgt(branchAndFinancialInstitutionIdentification);

        // TODO: Independizar
        paymentInstructionInformation.setChrgBr(ChargeBearerType1Code.SLEV);


        PartyIdentification32 partyIdentification321 = new PartyIdentification32();
        Party6Choice party6Choice = new Party6Choice();
        PersonIdentification5 personIdentification5 = new PersonIdentification5();

        PersonIdentificationSchemeName1Choice personIdentificationSchemeName1Choice = new PersonIdentificationSchemeName1Choice();
        personIdentificationSchemeName1Choice.setPrtry("SEPA");
        GenericPersonIdentification1 genericPersonIdentification1 = new GenericPersonIdentification1();
        genericPersonIdentification1.setSchmeNm(personIdentificationSchemeName1Choice);


        try {
            genericPersonIdentification1.setId(SepaUtils.identificadorUnicoDeInterviniente(sepaPago.getAcreedorNIF(), sepaPago.getAcreedorSufijo(), DEFAULT_COUNTRY));
        } catch (InvalidDataException e) {
            throw new StopProcessingException(e);
        }


        personIdentification5.getOthr().add(genericPersonIdentification1);

        party6Choice.setPrvtId(personIdentification5);
        partyIdentification321.setId(party6Choice);
        paymentInstructionInformation.setCdtrSchmeId(partyIdentification321);

        sepaOperacionCreator.process(sepaPago);
        Vector<SepaOperacion> sepaOperaciones = sepaOperacionCreator.getSepaOperaciones();
        DirectDebitTransactionInformation9 directDebitTransactionInformation;
        int dummyCounter;
        BigDecimal importeAcumulado ;
        for (SepaOperacion sepaOperacion : sepaOperaciones) {
            directDebitTransactionInformation = generaSepaOperacion(sepaFichero, sepaPago, sepaOperacion);

            paymentInstructionInformation.getDrctDbtTxInf().add(directDebitTransactionInformation);

            // Actualizamos los valores de numeros de transacciones e importe
            dummyCounter = new Integer(paymentInstructionInformation.getNbOfTxs()).intValue();
            paymentInstructionInformation.setNbOfTxs("" + (++dummyCounter));

            importeAcumulado = paymentInstructionInformation.getCtrlSum() ;
            importeAcumulado = importeAcumulado.add(directDebitTransactionInformation.getInstdAmt().getValue())  ;
            importeAcumulado = importeAcumulado.setScale(2) ;
            paymentInstructionInformation.setCtrlSum(importeAcumulado);

        }

        return paymentInstructionInformation;

    }

    private DirectDebitTransactionInformation9 generaSepaOperacion(SepaFichero sepaFichero, SepaPago sepaPago, SepaOperacion sepaOperacion) throws StopProcessingException {
        DirectDebitTransactionInformation9 transaction = new DirectDebitTransactionInformation9();
        PaymentIdentification1 paymentIdentification = new PaymentIdentification1();
        paymentIdentification.setInstrId(sepaOperacion.getIdOperacion());
        paymentIdentification.setEndToEndId(sepaOperacion.getIdOperacionExtremo());
        transaction.setPmtId(paymentIdentification);
        PaymentInstructionInformation4 paymentInstructionInformation = new PaymentInstructionInformation4();

        ActiveOrHistoricCurrencyAndAmount activeOrHistoricCurrencyAndAmount = new ActiveOrHistoricCurrencyAndAmount();
        activeOrHistoricCurrencyAndAmount.setCcy("EUR");
        activeOrHistoricCurrencyAndAmount.setValue(sepaOperacion.getImporte().setScale(2));
        transaction.setInstdAmt(activeOrHistoricCurrencyAndAmount);

        DirectDebitTransaction6 directDebitTransaction = new DirectDebitTransaction6();
        MandateRelatedInformation6 mandateRelatedInformation = new MandateRelatedInformation6();
        mandateRelatedInformation.setMndtId(sepaOperacion.getIdMandato());
        mandateRelatedInformation.setDtOfSgntr(SepaUtils.ISODate(sepaOperacion.getFechaDeMandato()));


        // Datos: Anterior y Modif
        // ---------

        if (!sepaOperacion.getIdModificacionDeMandato().equals("")) {
            mandateRelatedInformation.setAmdmntInd(Boolean.TRUE);
            AmendmentInformationDetails6 amendmentInformationDetails = new AmendmentInformationDetails6();
            amendmentInformationDetails.setOrgnlMndtId(sepaOperacion.getIdModificacionDeMandato());

            mandateRelatedInformation.setAmdmntInfDtls(amendmentInformationDetails);

            //

            PartyIdentification32 partyIdentification = new PartyIdentification32();
            partyIdentification.setNm(sepaOperacion.getNombreAnteriorDeAcreedor());

            Party6Choice partyChoice = new Party6Choice();
            PersonIdentification5 personIdentification = new PersonIdentification5();

            GenericPersonIdentification1 genericPersonIdentification1 = new GenericPersonIdentification1();
            PersonIdentificationSchemeName1Choice personIdentificationSchemeName1Choice = new PersonIdentificationSchemeName1Choice();
            personIdentificationSchemeName1Choice.setPrtry("SEPA");

            genericPersonIdentification1.setSchmeNm(personIdentificationSchemeName1Choice);
            genericPersonIdentification1.setId(sepaOperacion.getIdAnteriorDeAcreedor());

            personIdentification.getOthr().add(genericPersonIdentification1);
            partyChoice.setPrvtId(personIdentification);
            partyIdentification.setId(partyChoice);

            amendmentInformationDetails.setOrgnlCdtrSchmeId(partyIdentification);


            // Se se ha modificado la cuenta del deudor por otra en LA MISMA entidad
            if (sepaOperacion.getIBANAnteriorDeDeudor().substring(4, 7).equals(sepaOperacion.getIBANCuentaDeudor().substring(4, 7))) {
                CashAccount16 cashAccount = new CashAccount16();
                AccountIdentification4Choice accountIdentification4Choice = new AccountIdentification4Choice();
                accountIdentification4Choice.setIBAN(sepaOperacion.getIBANAnteriorDeDeudor());
                cashAccount.setId(accountIdentification4Choice);
                amendmentInformationDetails.setOrgnlDbtrAcct(cashAccount);

            } else {
                // Si se ha modificado la cuenta del deudos por otra en DISTINTA entidad
                BranchAndFinancialInstitutionIdentification4 branchAndFinancialInstitutionIdentification = new BranchAndFinancialInstitutionIdentification4();
                FinancialInstitutionIdentification7 financialInstitutionIdentification = new FinancialInstitutionIdentification7();

                GenericFinancialIdentification1 genericFinancialIdentification = new GenericFinancialIdentification1();
                genericFinancialIdentification.setId("SMNDA");
                financialInstitutionIdentification.setOthr(genericFinancialIdentification);

                branchAndFinancialInstitutionIdentification.setFinInstnId(financialInstitutionIdentification);
                amendmentInformationDetails.setOrgnlDbtrAgt(branchAndFinancialInstitutionIdentification);
            }

        }
        // -----


        BranchAndFinancialInstitutionIdentification4 branchAndFinancialInstitutionIdentification4 = new BranchAndFinancialInstitutionIdentification4();
        FinancialInstitutionIdentification7 financialInstitutionIdentification7 = new FinancialInstitutionIdentification7();
        financialInstitutionIdentification7.setBIC(sepaOperacion.getBICDeudor());
        branchAndFinancialInstitutionIdentification4.setFinInstnId(financialInstitutionIdentification7);
        transaction.setDbtrAgt(branchAndFinancialInstitutionIdentification4);

        PartyIdentification32 partyIdentification32 = new PartyIdentification32();
        partyIdentification32.setNm(sepaOperacion.getNombreDeudor());

        Party6Choice party6Choice = new Party6Choice();
        if (sepaOperacion.esDeudorPersonaJuridica()) {
            OrganisationIdentification4 organisationIdentification = new OrganisationIdentification4();
            OrganisationIdentificationSchemeName1Choice organisationIdentificationSchemeName1Choice = new OrganisationIdentificationSchemeName1Choice();
            organisationIdentificationSchemeName1Choice.setCd("CORE");
            GenericOrganisationIdentification1 genericOrganisationIdentification1 = new GenericOrganisationIdentification1();
            genericOrganisationIdentification1.setSchmeNm(organisationIdentificationSchemeName1Choice);

            try {
                genericOrganisationIdentification1.setId(SepaUtils.identificadorUnicoDeInterviniente(sepaOperacion.getNIFDeudor(), sepaOperacion.getSufijoDeudor(), "ES"));
            } catch (InvalidDataException e) {
                throw new StopProcessingException(e);
            }
            organisationIdentification.getOthr().add(genericOrganisationIdentification1);
            party6Choice.setOrgId(organisationIdentification);

        } else {
            PersonIdentification5 personIdentification5 = new PersonIdentification5();
            PersonIdentificationSchemeName1Choice personIdentificationSchemeName1Choice = new PersonIdentificationSchemeName1Choice();
            personIdentificationSchemeName1Choice.setCd("CORE");
            GenericPersonIdentification1 genericPersonIdentification1 = new GenericPersonIdentification1();
            genericPersonIdentification1.setSchmeNm(personIdentificationSchemeName1Choice);

            try {
                genericPersonIdentification1.setId(SepaUtils.identificadorUnicoDeInterviniente(sepaOperacion.getNIFDeudor(), sepaOperacion.getSufijoDeudor(), DEFAULT_COUNTRY));
            } catch (InvalidDataException e) {
                throw new StopProcessingException(e);
            }
            personIdentification5.getOthr().add(genericPersonIdentification1);
            party6Choice.setPrvtId(personIdentification5);
        }

        partyIdentification32.setId(party6Choice);
        transaction.setDbtr(partyIdentification32);

        CashAccount16 cashAccount16 = new CashAccount16();
        AccountIdentification4Choice accountIdentification4Choice = new AccountIdentification4Choice();
        accountIdentification4Choice.setIBAN(sepaOperacion.getIBANCuentaDeudor());
        cashAccount16.setId(accountIdentification4Choice);
        transaction.setDbtrAcct(cashAccount16);

        RemittanceInformation5 remittanceInformation5 = new RemittanceInformation5();
        remittanceInformation5.getUstrd().add(sepaOperacion.getConceptoDeOperacion());
        transaction.setRmtInf(remittanceInformation5);


        directDebitTransaction.setMndtRltdInf(mandateRelatedInformation);
        transaction.setDrctDbtTx(directDebitTransaction);


        return transaction;

    }


    public void write(File outputFile) throws JAXBException, IOException {
        write(outputFile, true);
    }

    public void write(String fileName) throws JAXBException, IOException {
        write(new File(fileName));
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
