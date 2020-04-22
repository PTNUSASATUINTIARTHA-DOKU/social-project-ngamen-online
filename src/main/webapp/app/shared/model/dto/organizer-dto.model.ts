import { IOrganizer } from 'app/shared/model/organizer.model';

export interface IOrganizerDTO {
  id?: number;
  name?: string;
  url?: string;
  email?: string;
}

export class Organizer implements IOrganizer {
  constructor(public id?: number, public name?: string, public url?: string, public email?: string) {}
}
