import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DonationSharedModule } from 'app/shared/shared.module';
import { TransactionComponent } from './transaction.component';
import { TransactionDetailComponent } from './transaction-detail.component';
import { transactionRoute } from './transaction.route';

@NgModule({
  imports: [DonationSharedModule, RouterModule.forChild(transactionRoute)],
  declarations: [TransactionComponent, TransactionDetailComponent]
})
export class DonationTransactionModule {}
