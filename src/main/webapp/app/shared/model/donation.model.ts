import { Moment } from 'moment';
import { IOrganizer } from 'app/shared/model/organizer.model';
import { ITransaction } from 'app/shared/model/transaction.model';
import { IsActiveStatus } from 'app/shared/model/enumerations/is-active-status.model';

export interface IDonation {
  id?: number;
  name?: string;
  description?: string;
  url?: string;
  imageUrl?: string;
  paymentUrl?: string;
  lastUpdatedBy?: string;
  lastUpdatedAt?: Moment;
  status?: IsActiveStatus;
  organizer?: IOrganizer;
  transactions?: ITransaction[];
}

export class Donation implements IDonation {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public url?: string,
    public imageUrl?: string,
    public paymentUrl?: string,
    public lastUpdatedBy?: string,
    public lastUpdatedAt?: Moment,
    public status?: IsActiveStatus,
    public organizer?: IOrganizer,
    public transactions?: ITransaction[]
  ) {}
}
