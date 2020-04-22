import { Moment } from 'moment';
import { IDonation } from 'app/shared/model/donation.model';
import { PaymentChannel } from 'app/shared/model/enumerations/payment-channel.model';
import { TransactionStatus } from 'app/shared/model/enumerations/transaction-status.model';

export interface ITransaction {
  id?: number;
  invoiceNumber?: string;
  sessionId?: string;
  basket?: string;
  ovoIdMasked?: string;
  deviceInformation?: string;
  name?: string;
  mobile?: string;
  email?: string;
  amount?: number;
  paymentChannel?: PaymentChannel;
  lastUpdatedBy?: string;
  lastUpdatedAt?: Moment;
  status?: TransactionStatus;
  donation?: IDonation;
}

export class Transaction implements ITransaction {
  constructor(
    public id?: number,
    public invoiceNumber?: string,
    public sessionId?: string,
    public basket?: string,
    public ovoIdMasked?: string,
    public deviceInformation?: string,
    public name?: string,
    public mobile?: string,
    public email?: string,
    public amount?: number,
    public paymentChannel?: PaymentChannel,
    public lastUpdatedBy?: string,
    public lastUpdatedAt?: Moment,
    public status?: TransactionStatus,
    public donation?: IDonation
  ) {}
}
