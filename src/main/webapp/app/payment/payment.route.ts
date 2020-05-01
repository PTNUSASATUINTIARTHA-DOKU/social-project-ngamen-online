import { HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router, Routes } from '@angular/router';
import { Donation } from 'app/shared/model/donation.model';
import { ITransaction, Transaction } from 'app/shared/model/transaction.model';
import { EMPTY, Observable, of } from 'rxjs';
import { catchError, flatMap } from 'rxjs/operators';
import { PaymentComponent } from './payment.component';
import { PaymentResultComponent } from './payment.result.component';
import { PaymentService } from './payment.service';

@Injectable({ providedIn: 'root' })
export class PaymentDonationResolver implements Resolve<Donation> {
  constructor(private paymentService: PaymentService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<Donation> {
    const slug = route.paramMap.get('slug');
    if (slug) {
      return this.paymentService.findSlug(slug).pipe(
        flatMap((donation: HttpResponse<Donation>) => {
          if (donation.body) {
            return of(donation.body);
          } else {
            return EMPTY;
          }
        })
      );
    }
    this.router.navigate(['404']);
    return EMPTY;
  }
}

@Injectable({ providedIn: 'root' })
export class PaymentResultResolver implements Resolve<ITransaction> {
  // subject: Subject<ITransaction>;
  result: Transaction;
  constructor(private paymentService: PaymentService, private router: Router) {
    // this.subject = new Subject<ITransaction>();
    this.result = {};
  }

  resolve(route: ActivatedRouteSnapshot): Observable<ITransaction> {
    const invoice = route.paramMap.get('invoice');
    if (invoice) {
      // eslint-disable-next-line no-console
      console.log(this.paymentService);
      return this.paymentService.findInvoice(invoice).pipe(
        flatMap((transaction: HttpResponse<Transaction>) => {
          if (transaction.body) {
            return of(transaction.body);
          } else {
            return EMPTY;
          }
        }),
        catchError(() => {
          this.paymentService.sharedResult.subscribe(result => (this.result = result));
          if (this.result.id !== void 0) return of(this.result);
          else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    } else {
      this.router.navigate(['404']);
      return EMPTY;
    }
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
    path: 'payment/:invoice/result',
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
