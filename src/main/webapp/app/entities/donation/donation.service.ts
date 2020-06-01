import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDonation } from 'app/shared/model/donation.model';

type EntityResponseType = HttpResponse<IDonation>;
type EntityArrayResponseType = HttpResponse<IDonation[]>;

@Injectable({ providedIn: 'root' })
export class DonationService {
  public resourceUrl = SERVER_API_URL + 'api/donations';

  constructor(protected http: HttpClient) {}

  create(donation: IDonation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(donation);
    return this.http
      .post<IDonation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(donation: IDonation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(donation);
    return this.http
      .put<IDonation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDonation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDonation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  sendEmail(id: number): Observable<HttpResponse<{}>> {
    return this.http.post<Boolean>(`${this.resourceUrl}/email`, id, { observe: 'response' });
  }

  protected convertDateFromClient(donation: IDonation): IDonation {
    const copy: IDonation = Object.assign({}, donation, {
      lastUpdatedAt: donation.lastUpdatedAt && donation.lastUpdatedAt.isValid() ? donation.lastUpdatedAt.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.lastUpdatedAt = res.body.lastUpdatedAt ? moment(res.body.lastUpdatedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((donation: IDonation) => {
        donation.lastUpdatedAt = donation.lastUpdatedAt ? moment(donation.lastUpdatedAt) : undefined;
      });
    }
    return res;
  }
}
