import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITransaction, Transaction } from 'app/shared/model/transaction.model';
import { TransactionService } from './transaction.service';
import { TransactionComponent } from './transaction.component';
import { TransactionDetailComponent } from './transaction-detail.component';

@Injectable({ providedIn: 'root' })
export class TransactionResolve implements Resolve<ITransaction> {
  constructor(private service: TransactionService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITransaction> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((transaction: HttpResponse<Transaction>) => {
          if (transaction.body) {
            return of(transaction.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Transaction());
  }
}

export const transactionRoute: Routes = [
  {
    path: '',
    component: TransactionComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,desc',
      pageTitle: 'donationApp.transaction.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TransactionDetailComponent,
    resolve: {
      transaction: TransactionResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'donationApp.transaction.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
