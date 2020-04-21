import { HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router, Routes } from '@angular/router';
import { DonationService } from 'app/entities/donation/donation.service';
import { Donation, IDonation } from 'app/shared/model/donation.model';
import { EMPTY, Observable, of } from 'rxjs';
import { flatMap } from 'rxjs/operators';
import { PaymentComponent } from './payment.component';

@Injectable({ providedIn: 'root' })
export class PaymentResolver implements Resolve<IDonation> {
  constructor(private donationService: DonationService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDonation> {
    const slug = route.paramMap.get('slug');
    if (slug) {
      return this.donationService.query({ paymentSlug: slug }).pipe(
        flatMap((donations: HttpResponse<Donation[]>) => {
          if (donations.body && donations.body.length === 1) {
            return of(donations.body[0]);
          }
          this.router.navigate(['404']);
          return EMPTY;
        })
      );
    }
    return EMPTY;
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
