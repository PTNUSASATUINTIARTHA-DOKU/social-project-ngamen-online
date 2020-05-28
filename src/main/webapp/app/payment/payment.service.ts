import { HttpClient, HttpResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { IDonation } from 'app/shared/model/donation.model';
import { ITransaction, Transaction } from 'app/shared/model/transaction.model';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class PaymentService {
  public resourceUrl = SERVER_API_URL + 'api/payments';
  private result = new BehaviorSubject(new Transaction());
  sharedResult = this.result.asObservable();

  constructor(protected http: HttpClient) {}

  findSlug(slug: string): Observable<HttpResponse<IDonation>> {
    return this.http.get<IDonation>(`${this.resourceUrl}/${slug}/slug`, { observe: 'response' });
  }

  findInvoice(invoice: string): Observable<HttpResponse<ITransaction>> {
    return this.http.get<ITransaction>(`${this.resourceUrl}/${invoice}/invoice`, { observe: 'response' });
  }

  initPayment(transaction: Transaction): Observable<HttpResponse<ITransaction>> {
    return this.http.post<ITransaction>(`${this.resourceUrl}`, transaction, {
      observe: 'response',
      headers: new HttpHeaders({ timeout: `${90000}` })
    });
  }

  checkRecaptcha(token: string): Observable<HttpResponse<Boolean>> {
    return this.http.post<Boolean>(`${this.resourceUrl}/recaptcha`, token, { observe: 'response' });
  }

  paymentResult(transaction: Transaction): void {
    this.result.next(transaction);
  }
}
