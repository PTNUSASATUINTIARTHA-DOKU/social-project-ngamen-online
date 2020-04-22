import { IOrganizerDTO } from './organizer-dto.model';

export interface IDonationDTO {
  id?: number;
  name?: string;
  description?: string;
  url?: string;
  imageUrl?: string;
  paymentSlug?: string;
  organizer?: IOrganizerDTO;
}

export class DonationDTO implements IDonationDTO {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public url?: string,
    public imageUrl?: string,
    public paymentSlug?: string,
    public organizer?: IOrganizerDTO
  ) {}
}
