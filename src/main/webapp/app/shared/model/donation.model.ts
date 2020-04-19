import { Moment } from 'moment';
import { ITransaction } from 'app/shared/model/transaction.model';
import { IOrganizer } from 'app/shared/model/organizer.model';
import { IsActiveStatus } from 'app/shared/model/enumerations/is-active-status.model';

export interface IDonation {
  id?: number;
  name?: string;
  description?: string;
  url?: string;
  imageUrl?: string;
  paymentUrl?: string;
  bankAccountNumber?: string;
  bankAccountName?: string;
  bankName?: string;
  lastUpdatedBy?: string;
  lastUpdatedAt?: Moment;
  status?: IsActiveStatus;
  transactions?: ITransaction[];
  organizer?: IOrganizer;
}

export class Donation implements IDonation {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public url?: string,
    public imageUrl?: string,
    public paymentUrl?: string,
    public bankAccountNumber?: string,
    public bankAccountName?: string,
    public bankName?: string,
    public lastUpdatedBy?: string,
    public lastUpdatedAt?: Moment,
    public status?: IsActiveStatus,
    public transactions?: ITransaction[],
    public organizer?: IOrganizer
  ) {}
}
