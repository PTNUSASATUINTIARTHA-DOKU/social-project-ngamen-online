import { Component, ViewEncapsulation } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDonation } from 'app/shared/model/donation.model';
import { DonationService } from './donation.service';

@Component({
  templateUrl: './donation-delete-dialog.component.html',
  encapsulation: ViewEncapsulation.None
})
export class DonationDeleteDialogComponent {
  donation?: IDonation;

  constructor(protected donationService: DonationService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.donationService.delete(id).subscribe(() => {
      this.eventManager.broadcast('donationListModification');
      this.activeModal.close();
    });
  }
}
