import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { IPaymentDTO } from 'app/shared/model/dto/payment-dto.model';
import { Observable } from 'rxjs';

type EntityResponseType = HttpResponse<IPaymentDTO>;

@Injectable({ providedIn: 'root' })
export class PaymentService {
  public resourceUrl = SERVER_API_URL + 'api/payments';

  constructor(protected http: HttpClient) {}

  find(slug: string): Observable<EntityResponseType> {
    return this.http.get<IPaymentDTO>(`${this.resourceUrl}/${slug}`, { observe: 'response' });
  }
}
