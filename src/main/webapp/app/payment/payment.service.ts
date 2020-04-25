import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { IPaymentDTO, PaymentDTO } from 'app/shared/model/dto/payment-dto.model';
import { Observable, BehaviorSubject } from 'rxjs';

type EntityResponseType = HttpResponse<IPaymentDTO>;

@Injectable({ providedIn: 'root' })
export class PaymentService {
  public resourceUrl = SERVER_API_URL + 'api/payments';
  private result = new BehaviorSubject(new PaymentDTO());
  sharedResult = this.result.asObservable();

  constructor(protected http: HttpClient) {}

  find(slug: string): Observable<EntityResponseType> {
    return this.http.get<IPaymentDTO>(`${this.resourceUrl}/${slug}`, { observe: 'response' });
  }

  initPayment(payment: PaymentDTO): Observable<EntityResponseType> {
    return this.http.post<IPaymentDTO>(`${this.resourceUrl}`, payment, { observe: 'response' });
  }

  paymentResult(payment: PaymentDTO): void {
    this.result.next(payment);
  }
}
