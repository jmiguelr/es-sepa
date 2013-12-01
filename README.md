pt-sepa-iso20022
================

## Create Credit Transfer

CreditTransfer transfer = new CreditTransfer(idFile, companyName);

CreditTransferPaymentGroup group = new CreditTransferPaymentGroup(idGroup, executionDate, companyName, companyIban, companyBic);

group.addTransaction(idTrans, value, creditorName, creditorIban, creditorBic);

transfer.addPaymentGroup(group);

transfer.write(fileName);

