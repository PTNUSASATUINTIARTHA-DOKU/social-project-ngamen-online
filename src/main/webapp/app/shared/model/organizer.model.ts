import { Moment } from 'moment';
import { IDonation } from 'app/shared/model/donation.model';
import { IUser } from 'app/core/user/user.model';
import { IsActiveStatus } from 'app/shared/model/enumerations/is-active-status.model';

export interface IOrganizer {
  id?: number;
  name?: string;
  url?: string;
  email?: string;
  bankAccountNumber?: string;
  bankAccountName?: string;
  bankName?: string;
  mdr?: number;
  sharing?: number;
  lastUpdatedBy?: string;
  lastUpdatedAt?: Moment;
  mallId?: number;
  sharedKey?: string;
  serviceId?: number;
  acquirerId?: number;
  status?: IsActiveStatus;
  donations?: IDonation[];
  users?: IUser[];
}

export class Organizer implements IOrganizer {
  constructor(
    public id?: number,
    public name?: string,
    public url?: string,
    public email?: string,
    public bankAccountNumber?: string,
    public bankAccountName?: string,
    public bankName?: string,
    public mdr?: number,
    public sharing?: number,
    public lastUpdatedBy?: string,
    public lastUpdatedAt?: Moment,
    public mallId?: number,
    public sharedKey?: string,
    public serviceId?: number,
    public acquirerId?: number,
    public status?: IsActiveStatus,
    public donations?: IDonation[],
    public users?: IUser[]
  ) {}
}
