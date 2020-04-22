import { HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router, Routes } from '@angular/router';
import { IPaymentDTO, PaymentDTO } from 'app/shared/model/dto/payment-dto.model';
import { EMPTY, Observable, of } from 'rxjs';
import { flatMap } from 'rxjs/operators';
import { PaymentComponent } from './payment.component';
import { PaymentService } from './payment.service';

@Injectable({ providedIn: 'root' })
export class PaymentResolver implements Resolve<IPaymentDTO> {
  constructor(private paymentService: PaymentService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPaymentDTO> {
    const slug = route.paramMap.get('slug');
    if (slug) {
      return this.paymentService.find(slug).pipe(
        flatMap((payment: HttpResponse<PaymentDTO>) => {
          if (payment.body) {
            return of(payment.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PaymentDTO());
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
      payment: PaymentResolver
    },
    data: {
      authorities: [],
      pageTitle: 'payment.title'
    }
  }
];
