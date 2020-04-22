import { ITransaction, Transaction } from 'app/shared/model/transaction.model';
import { IDonationDTO, DonationDTO } from 'app/shared/model/dto/donation-dto.model';

export interface IPaymentDTO {
  donationDTO?: IDonationDTO;
  transaction?: ITransaction;
}

export class PaymentDTO implements IPaymentDTO {
  constructor(public donation?: DonationDTO, public transaction?: Transaction) {}
}
