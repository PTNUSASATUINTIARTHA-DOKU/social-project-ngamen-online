import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DonationSharedModule } from 'app/shared/shared.module';
import { PAYMENT_ROUTE } from './payment.route';
import { PaymentComponent } from './payment.component';
import { PaymentResultComponent } from './payment.result.component';
import { DeviceDetectorModule } from 'ngx-device-detector';
import { PaymentOfflineComponent } from './payment.offline.component';

@NgModule({
  imports: [DonationSharedModule, RouterModule.forChild(PAYMENT_ROUTE), DeviceDetectorModule.forRoot()],
  declarations: [PaymentComponent, PaymentResultComponent, PaymentOfflineComponent]
})
export class DonationPaymentModule {}
