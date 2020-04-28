import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IOrganizer, Organizer } from 'app/shared/model/organizer.model';
import { OrganizerService } from './organizer.service';
import { OrganizerComponent } from './organizer.component';
import { OrganizerDetailComponent } from './organizer-detail.component';
import { OrganizerUpdateComponent } from './organizer-update.component';

@Injectable({ providedIn: 'root' })
export class OrganizerResolve implements Resolve<IOrganizer> {
  constructor(private service: OrganizerService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOrganizer> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((organizer: HttpResponse<Organizer>) => {
          if (organizer.body) {
            return of(organizer.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Organizer());
  }
}

export const organizerRoute: Routes = [
  {
    path: '',
    component: OrganizerComponent,
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'donationApp.organizer.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: OrganizerDetailComponent,
    resolve: {
      organizer: OrganizerResolve
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'donationApp.organizer.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: OrganizerUpdateComponent,
    resolve: {
      organizer: OrganizerResolve
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'donationApp.organizer.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: OrganizerUpdateComponent,
    resolve: {
      organizer: OrganizerResolve
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'donationApp.organizer.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
