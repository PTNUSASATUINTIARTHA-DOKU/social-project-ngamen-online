import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DonationSharedModule } from 'app/shared/shared.module';
import { DonationComponent } from './donation.component';
import { DonationDetailComponent } from './donation-detail.component';
import { DonationUpdateComponent } from './donation-update.component';
import { DonationDeleteDialogComponent } from './donation-delete-dialog.component';
import { donationRoute } from './donation.route';

@NgModule({
  imports: [DonationSharedModule, RouterModule.forChild(donationRoute)],
  declarations: [DonationComponent, DonationDetailComponent, DonationUpdateComponent, DonationDeleteDialogComponent],
  entryComponents: [DonationDeleteDialogComponent]
})
export class DonationDonationModule {}
