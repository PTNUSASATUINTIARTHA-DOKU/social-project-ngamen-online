import { Route } from '@angular/router';

import { PaymentComponent } from './payment.component';

export const PAYMENT_ROUTE: Route = {
  path: 'payment',
  component: PaymentComponent,
  data: {
    authorities: [],
    pageTitle: 'home.title'
  }
};
