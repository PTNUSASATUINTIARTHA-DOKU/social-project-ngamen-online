import { HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router, Routes } from '@angular/router';
import { Donation, IDonation } from 'app/shared/model/donation.model';
import { EMPTY, Observable, of } from 'rxjs';
import { flatMap } from 'rxjs/operators';
import { PaymentComponent } from './payment.component';
import { PaymentService } from './payment.service';

@Injectable({ providedIn: 'root' })
export class PaymentResolver implements Resolve<IDonation> {
  constructor(private paymentService: PaymentService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDonation> {
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
    return of(new Donation());
  }
}

export const PAYMENT_ROUTE: Routes = [
  {
    path: 'payment',
    component: PaymentComponent,
    data: {
      authorities: [],
      pageTitle: 'payment.title'
    }
  },
  {
    path: 'payment/:slug',
    component: PaymentComponent,
    resolve: {
      donation: PaymentResolver
    },
    data: {
      authorities: [],
      pageTitle: 'payment.title'
    }
  }
];
