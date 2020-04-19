import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DonationSharedModule } from 'app/shared/shared.module';
import { OrganizerComponent } from './organizer.component';
import { OrganizerDetailComponent } from './organizer-detail.component';
import { OrganizerUpdateComponent } from './organizer-update.component';
import { OrganizerDeleteDialogComponent } from './organizer-delete-dialog.component';
import { organizerRoute } from './organizer.route';

@NgModule({
  imports: [DonationSharedModule, RouterModule.forChild(organizerRoute)],
  declarations: [OrganizerComponent, OrganizerDetailComponent, OrganizerUpdateComponent, OrganizerDeleteDialogComponent],
  entryComponents: [OrganizerDeleteDialogComponent]
})
export class DonationOrganizerModule {}
