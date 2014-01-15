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
 * Based on original work from: https://github.com/joaoosorio/pt-sepa-iso20022
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

        GroupHeader39 groupHeader = getGroupHeader(sepaFichero);

        // Add group header to document
        document.getCstmrDrctDbtInitn().setGrpHdr(groupHeader);


        // Comienza el proceso de pagos
        sepaPagoCreator.process(sepaFichero);
        Vector<SepaPago> sepaPagos = sepaPagoCreator.getSepaPagos();
        for (SepaPago sepaPago : sepaPagos) {
            addSepaPago(sepaFichero, sepaPago);

        }

        // TODO: Actualizar el numero de transacciones, CheckSums...

    }


    private void addSepaPago(SepaFichero sepaFichero, SepaPago sepaPago) throws StopProcessingException {


        PaymentInstructionInformation4 paymentInstructionInformation = new PaymentInstructionInformation4();
        paymentInstructionInformation.setPmtInfId(sepaPago.getIdPago());


        document.getCstmrDrctDbtInitn().getPmtInf().add(paymentInstructionInformation);

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
            genericPersonIdentification1.setId(SepaUtils.identificadorUnicoDeInterviniente(sepaPago.getAcreedorNIF(), sepaPago.getAcreedorSufijo(), "ES"));
        } catch (InvalidDataException e) {
            throw new StopProcessingException(e);
        }


        personIdentification5.getOthr().add(genericPersonIdentification1);

        party6Choice.setPrvtId(personIdentification5);
        partyIdentification321.setId(party6Choice);
        paymentInstructionInformation.setCdtrSchmeId(partyIdentification321);

        sepaOperacionCreator.process(sepaPago);
        Vector<SepaOperacion> sepaOperaciones = sepaOperacionCreator.getSepaOperaciones();

        for (SepaOperacion sepaOperacion : sepaOperaciones) {
            addSepaOperacion(sepaFichero, sepaPago, sepaOperacion);
        }


    }

    private void addSepaOperacion(SepaFichero sepaFichero, SepaPago sepaPago, SepaOperacion sepaOperacion) throws StopProcessingException {

        DirectDebitTransactionInformation9 transaction = new DirectDebitTransactionInformation9();
        PaymentIdentification1 paymentIdentification = new PaymentIdentification1();
        paymentIdentification.setInstrId(sepaOperacion.getIdOperacion());
        paymentIdentification.setEndToEndId(sepaOperacion.getIdOperacionExtremo());
        transaction.setPmtId(paymentIdentification);
        PaymentInstructionInformation4 paymentInstructionInformation = new PaymentInstructionInformation4();

        ActiveOrHistoricCurrencyAndAmount activeOrHistoricCurrencyAndAmount = new ActiveOrHistoricCurrencyAndAmount();
        activeOrHistoricCurrencyAndAmount.setCcy("EUR");
        activeOrHistoricCurrencyAndAmount.setValue(sepaOperacion.getImporte());
        transaction.setInstdAmt(activeOrHistoricCurrencyAndAmount);

        DirectDebitTransaction6 directDebitTransaction = new DirectDebitTransaction6();
        MandateRelatedInformation6 mandateRelatedInformation = new MandateRelatedInformation6();
        mandateRelatedInformation.setMndtId(sepaOperacion.getIdMandato());
        mandateRelatedInformation.setDtOfSgntr(SepaUtils.ISODate(sepaOperacion.getFechaDeMandato()));


        // Datos: Anterior y Modif


        // mandateRelatedInformation.setAmdmntInd( );  // Todo: AmdmntInd ¿Como se si hay que incluir esta etiqueta?

        // TODO: Igual que antes,¿como se si hay que incluir?  ¿Asi que tal?

        // ---------
        /*
        if( !sepaOperacion.getIdModificacionDeMandato().equals("") ) {
            AmendmentInformationDetails6 amendmentInformationDetails = new AmendmentInformationDetails6() ;
            amendmentInformationDetails.setOrgnlMndtId( sepaOperacion.getIdModificacionDeMandato() );
            PartyIdentification32 partyIdentification32 = new PartyIdentification32();
            partyIdentification32.setNm( sepaOperacion.getNombreAnteriorDeAcreedor());
            Party6Choice party6Choice = new Party6Choice();
            PersonIdentification5 personIdentification5 = new PersonIdentification5() ;
            GenericPersonIdentification1 genericPersonIdentification1 = new GenericPersonIdentification1() ;


            genericPersonIdentification1.setSchmeNm(  );
            genericPersonIdentification1.setId(sepaPago.getAcreedorNIF());     // Todo: ??


            personIdentification5.getOthr().add( genericPersonIdentification1 ) ;

            party6Choice.setPrvtId(personIdentification5);


            partyIdentification32.setId(party6Choice );
            amendmentInformationDetails.setOrgnlCdtrSchmeId(partyIdentification32);
            mandateRelatedInformation.setAmdmntInfDtls( amendmentInformationDetails );

        }
*/


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
        } else {
            PersonIdentification5 personIdentification5 = new PersonIdentification5();
            PersonIdentificationSchemeName1Choice personIdentificationSchemeName1Choice = new PersonIdentificationSchemeName1Choice();
            personIdentificationSchemeName1Choice.setCd("CORE");
            GenericPersonIdentification1 genericPersonIdentification1 = new GenericPersonIdentification1();

            genericPersonIdentification1.setSchmeNm(personIdentificationSchemeName1Choice);
            try {
                genericPersonIdentification1.setId(SepaUtils.identificadorUnicoDeInterviniente(sepaOperacion.getNIFDeudor(), sepaOperacion.getSufijoDeudor(), "ES"));
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


        paymentInstructionInformation.getDrctDbtTxInf().add(transaction);
        document.getCstmrDrctDbtInitn().getPmtInf().add(paymentInstructionInformation);


    }


    private GroupHeader39 getGroupHeader(SepaFichero sepaFichero) throws StopProcessingException {
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
            genericOrganisationIdentification1.setId(sepaFichero.getPresentadorNIF());    // Todo: ¿Es este campo?

            organisationIdentification.getOthr().add(genericOrganisationIdentification1);

            party6Choice.setOrgId(organisationIdentification);

        } else {
            PersonIdentification5 personIdentification5 = new PersonIdentification5();
            PersonIdentificationSchemeName1Choice personIdentificationSchemeName1Choice = new PersonIdentificationSchemeName1Choice();
            personIdentificationSchemeName1Choice.setCd("CORE");
            GenericPersonIdentification1 genericPersonIdentification1 = new GenericPersonIdentification1();

            genericPersonIdentification1.setSchmeNm(personIdentificationSchemeName1Choice);
            genericPersonIdentification1.setId(sepaFichero.getPresentadorNIF()); // Todo: ¿Es este campo?
            try {
                genericPersonIdentification1.setId(SepaUtils.identificadorUnicoDeInterviniente(sepaFichero.getPresentadorNIF(), sepaFichero.getPresentadorSufijo(), "ES"));
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


    /**
     * @throws java.io.IOException
     */
    public void write(File outputFile) throws JAXBException, IOException {
        FileWriter file = new FileWriter(outputFile);
        JAXBContext jc = JAXBContext.newInstance(Document.class);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.marshal(new ObjectFactory().createDocument(document), new BufferedWriter(file));
        file.close();
    }

    public void write(String fileName) throws JAXBException, IOException {
        write(new File(fileName));
    }

}
