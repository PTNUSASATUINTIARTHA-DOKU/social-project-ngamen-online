import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DonationSharedModule } from 'app/shared/shared.module';
import { PAYMENT_ROUTE } from './payment.route';
import { PaymentComponent } from './payment.component';
import { PaymentResultComponent } from './payment.result.component';
import { DeviceDetectorModule } from 'ngx-device-detector';

@NgModule({
  imports: [DonationSharedModule, RouterModule.forChild(PAYMENT_ROUTE), DeviceDetectorModule.forRoot()],
  declarations: [PaymentComponent, PaymentResultComponent]
})
export class DonationPaymentModule {}
