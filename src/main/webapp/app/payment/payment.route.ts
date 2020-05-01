import { HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router, Routes } from '@angular/router';
import { Donation } from 'app/shared/model/donation.model';
import { ITransaction } from 'app/shared/model/transaction.model';
import { EMPTY, Observable, of } from 'rxjs';
import { flatMap } from 'rxjs/operators';
import { PaymentComponent } from './payment.component';
import { PaymentResultComponent } from './payment.result.component';
import { PaymentService } from './payment.service';

@Injectable({ providedIn: 'root' })
export class PaymentDonationResolver implements Resolve<Donation> {
  constructor(private paymentService: PaymentService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<Donation> {
    const slug = route.paramMap.get('slug');
    if (slug) {
      return this.paymentService.find(slug).pipe(
        flatMap((donation: HttpResponse<Donation>) => {
          if (donation.body) {
            return of(donation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return EMPTY;
  }
}

@Injectable({ providedIn: 'root' })
export class PaymentResultResolver implements Resolve<ITransaction> {
  result: ITransaction;
  constructor(private paymentService: PaymentService, private router: Router) {
    this.result = {};
  }

  resolve(route: ActivatedRouteSnapshot): Observable<ITransaction> {
    const slug = route.paramMap.get('slug');
    if (slug) {
      this.paymentService.sharedResult.subscribe(result => (this.result = result));
      if (this.result !== void 0) {
        return of(this.result);
      } else {
        this.router.navigate(['404']);
        return EMPTY;
      }
    }
    return EMPTY;
  }
}

export const PAYMENT_ROUTE: Routes = [
  {
    path: 'payment/:slug',
    component: PaymentComponent,
    resolve: {
      donation: PaymentDonationResolver
    },
    data: {
      pageTitle: 'payment.title'
    }
  },
  {
    path: 'payment/:slug/result',
    component: PaymentResultComponent,
    resolve: {
      transaction: PaymentResultResolver
    },
    data: {
      pageTitle: 'payment.result.title'
    }
  },
  {
    path: 'payment',
    redirectTo: '404'
  },
  {
    path: 'payment/**',
    redirectTo: '404'
  }
];
