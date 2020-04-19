import { Moment } from 'moment';
import { IDonation } from 'app/shared/model/donation.model';
import { TransactionStatus } from 'app/shared/model/enumerations/transaction-status.model';

export interface ITransaction {
  id?: number;
  invoiceNumber?: string;
  sessionId?: string;
  basket?: string;
  ovoIdMasked?: string;
  deviceInformation?: string;
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
    public lastUpdatedBy?: string,
    public lastUpdatedAt?: Moment,
    public status?: TransactionStatus,
    public donation?: IDonation
  ) {}
}
