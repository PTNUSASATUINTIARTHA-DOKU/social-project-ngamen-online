import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IDonation, Donation } from 'app/shared/model/donation.model';
import { DonationService } from './donation.service';
import { DonationComponent } from './donation.component';
import { DonationDetailComponent } from './donation-detail.component';
import { DonationUpdateComponent } from './donation-update.component';

@Injectable({ providedIn: 'root' })
export class DonationResolve implements Resolve<IDonation> {
  constructor(private service: DonationService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDonation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
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

export const donationRoute: Routes = [
  {
    path: '',
    component: DonationComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'donationApp.donation.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DonationDetailComponent,
    resolve: {
      donation: DonationResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'donationApp.donation.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DonationUpdateComponent,
    resolve: {
      donation: DonationResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'donationApp.donation.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DonationUpdateComponent,
    resolve: {
      donation: DonationResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'donationApp.donation.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
