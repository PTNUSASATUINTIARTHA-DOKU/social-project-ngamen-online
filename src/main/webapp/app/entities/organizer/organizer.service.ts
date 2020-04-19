import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IOrganizer } from 'app/shared/model/organizer.model';

type EntityResponseType = HttpResponse<IOrganizer>;
type EntityArrayResponseType = HttpResponse<IOrganizer[]>;

@Injectable({ providedIn: 'root' })
export class OrganizerService {
  public resourceUrl = SERVER_API_URL + 'api/organizers';

  constructor(protected http: HttpClient) {}

  create(organizer: IOrganizer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(organizer);
    return this.http
      .post<IOrganizer>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(organizer: IOrganizer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(organizer);
    return this.http
      .put<IOrganizer>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IOrganizer>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOrganizer[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(organizer: IOrganizer): IOrganizer {
    const copy: IOrganizer = Object.assign({}, organizer, {
      lastUpdatedAt: organizer.lastUpdatedAt && organizer.lastUpdatedAt.isValid() ? organizer.lastUpdatedAt.toJSON() : undefined
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
      res.body.forEach((organizer: IOrganizer) => {
        organizer.lastUpdatedAt = organizer.lastUpdatedAt ? moment(organizer.lastUpdatedAt) : undefined;
      });
    }
    return res;
  }
}
