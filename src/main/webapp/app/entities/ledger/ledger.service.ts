import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ILedger } from 'app/shared/model/ledger.model';

type EntityResponseType = HttpResponse<ILedger>;
type EntityArrayResponseType = HttpResponse<ILedger[]>;

@Injectable({ providedIn: 'root' })
export class LedgerService {
  public resourceUrl = SERVER_API_URL + 'api/ledgers';

  constructor(protected http: HttpClient) {}

  create(ledger: ILedger): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ledger);
    return this.http
      .post<ILedger>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(ledger: ILedger): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ledger);
    return this.http
      .put<ILedger>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILedger>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILedger[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(ledger: ILedger): ILedger {
    const copy: ILedger = Object.assign({}, ledger, {
      creationDate: ledger.creationDate && ledger.creationDate.isValid() ? ledger.creationDate.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.creationDate = res.body.creationDate ? moment(res.body.creationDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((ledger: ILedger) => {
        ledger.creationDate = ledger.creationDate ? moment(ledger.creationDate) : undefined;
      });
    }
    return res;
  }
}
