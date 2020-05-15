import { Component, ViewEncapsulation } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IOrganizer } from 'app/shared/model/organizer.model';
import { OrganizerService } from './organizer.service';

@Component({
  templateUrl: './organizer-delete-dialog.component.html',
  encapsulation: ViewEncapsulation.None
})
export class OrganizerDeleteDialogComponent {
  organizer?: IOrganizer;

  constructor(protected organizerService: OrganizerService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.organizerService.delete(id).subscribe(() => {
      this.eventManager.broadcast('organizerListModification');
      this.activeModal.close();
    });
  }
}
