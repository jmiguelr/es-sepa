package es.virtualsw.sepa;

import iso.std.iso._20022.tech.xsd.pain_008_001_02.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Original author comment:
 * <p/>
 * Helper class to build a Direct Debit payment group. A payment group holds a collection of payment transactions.
 *
 * @author Joao Osorio (joao.osorio@sibace.pt)
 *         <p/>
 *         <p/>
 *         Comment from jmiguel: PLEASE NOTE THAT THIS CLASS LOOKS LIKE IT'S NOT FINISHED BY ORIGINAL AUTHOR
 *         It's untouched from original repo https://github.com/joaoosorio/pt-sepa-iso20022 just as reference
 */
public class DirectDebitPaymentGroup {

    PaymentInstructionInformation4 paymentGroup;

    public DirectDebitPaymentGroup(String pmtInfId, Date reqDate,
                                   String creditorName, String creditorId,
                                   String creditorIBAN, String creditorBIC) {
        paymentGroup = new PaymentInstructionInformation4();

        // Set payment information id
        paymentGroup.setPmtInfId(pmtInfId);

        // Set payment method - always DD
        paymentGroup.setPmtMtd(PaymentMethod2Code.DD);

        // Initialize counters
        paymentGroup.setNbOfTxs("0");
        paymentGroup.setCtrlSum(BigDecimal.ZERO);

        // Set requested collection date
        paymentGroup.setReqdColltnDt(SepaUtils.ISODate(reqDate));

        // Set creditor
        paymentGroup.setCdtr(createParty(creditorName));

        // Set creditor account
        paymentGroup.setCdtrAcct(createAccount(creditorIBAN));

        // Set creditor agent
        paymentGroup.setCdtrAgt(createAgent(creditorBIC));

        // Set creditor scheme id
        paymentGroup.setCdtrSchmeId(createPartyId(creditorId));
    }

    public PaymentInstructionInformation4 getInformation() {
        return paymentGroup;
    }

    public void addTransaction(String endToEndIdentification, BigDecimal amount,
                               String mandateId, Date mandateDate,
                               String debtorName, String debtorIBAN, String debtorBIC) {
        DirectDebitTransactionInformation9 transaction = new DirectDebitTransactionInformation9();

        // Set transaction id
        PaymentIdentification1 pmtId = new PaymentIdentification1();
        pmtId.setEndToEndId(endToEndIdentification);
        transaction.setPmtId(pmtId);

        // Set transaction amount
        ActiveOrHistoricCurrencyAndAmount instdAmt = new ActiveOrHistoricCurrencyAndAmount();
        instdAmt.setValue(amount);
        instdAmt.setCcy("EUR");
        transaction.setInstdAmt(instdAmt);

        // Set direct debit transaction info (mandate related)
        // TODO How to proceed for changes to the mandate (AmdmntInd==true) and how to reflect it in an easy way. Create a new method, ie: changePayment?
        DirectDebitTransaction6 drctDbtTx = new DirectDebitTransaction6();
        MandateRelatedInformation6 mndRltdInf = new MandateRelatedInformation6();
        mndRltdInf.setMndtId(mandateId);
        mndRltdInf.setDtOfSgntr(SepaUtils.ISODate(mandateDate));
        drctDbtTx.setMndtRltdInf(mndRltdInf);
        transaction.setDrctDbtTx(drctDbtTx);

        // Set debtor
        transaction.setDbtr(createParty(debtorName));

        // Set creditor account
        transaction.setDbtrAcct(createAccount(debtorIBAN));

        // Set debtor agent
        transaction.setDbtrAgt(createAgent(debtorBIC));

        // Add transaction to payment group
        paymentGroup.getDrctDbtTxInf().add(transaction);

        // Update counters
        int numTxs = Integer.parseInt(paymentGroup.getNbOfTxs()) + 1;
        paymentGroup.setNbOfTxs(Integer.toString(numTxs));
        paymentGroup.setCtrlSum(paymentGroup.getCtrlSum().add(amount));
    }

    private PartyIdentification32 createParty(String name) {
        PartyIdentification32 party = new PartyIdentification32();
        party.setNm(name);
        return party;
    }

    private PartyIdentification32 createPartyId(String id) {
        PartyIdentification32 party = new PartyIdentification32();
        Party6Choice partyId = new Party6Choice();
        PersonIdentification5 prvtId = new PersonIdentification5();
        GenericPersonIdentification1 othrId = new GenericPersonIdentification1();
        othrId.setId(id);
        prvtId.getOthr().add(othrId);
        partyId.setPrvtId(prvtId);
        party.setId(partyId);
        return party;
    }

    private CashAccount16 createAccount(String iban) {
        CashAccount16 account = new CashAccount16();
        AccountIdentification4Choice accountId = new AccountIdentification4Choice();
        accountId.setIBAN(iban);
        account.setId(accountId);
        return account;
    }

    private BranchAndFinancialInstitutionIdentification4 createAgent(String bic) {
        BranchAndFinancialInstitutionIdentification4 agent = new BranchAndFinancialInstitutionIdentification4();
        FinancialInstitutionIdentification7 finId = new FinancialInstitutionIdentification7();
        finId.setBIC(bic);
        agent.setFinInstnId(finId);
        return agent;
    }
}
