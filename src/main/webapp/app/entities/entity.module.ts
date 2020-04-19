import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'organizer',
        loadChildren: () => import('./organizer/organizer.module').then(m => m.DonationOrganizerModule)
      },
      {
        path: 'donation',
        loadChildren: () => import('./donation/donation.module').then(m => m.DonationDonationModule)
      },
      {
        path: 'transaction',
        loadChildren: () => import('./transaction/transaction.module').then(m => m.DonationTransactionModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class DonationEntityModule {}
