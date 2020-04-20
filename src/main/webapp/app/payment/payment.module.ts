import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DonationSharedModule } from 'app/shared/shared.module';
import { PAYMENT_ROUTE } from './payment.route';
import { PaymentComponent } from './payment.component';

@NgModule({
  imports: [DonationSharedModule, RouterModule.forChild([PAYMENT_ROUTE])],
  declarations: [PaymentComponent]
})
export class DonationPaymentModule {}
