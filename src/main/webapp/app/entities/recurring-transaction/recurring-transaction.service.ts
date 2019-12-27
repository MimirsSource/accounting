import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IRecurringTransaction } from 'app/shared/model/recurring-transaction.model';

type EntityResponseType = HttpResponse<IRecurringTransaction>;
type EntityArrayResponseType = HttpResponse<IRecurringTransaction[]>;

@Injectable({ providedIn: 'root' })
export class RecurringTransactionService {
  public resourceUrl = SERVER_API_URL + 'api/recurring-transactions';

  constructor(protected http: HttpClient) {}

  create(recurringTransaction: IRecurringTransaction): Observable<EntityResponseType> {
    return this.http.post<IRecurringTransaction>(this.resourceUrl, recurringTransaction, { observe: 'response' });
  }

  update(recurringTransaction: IRecurringTransaction): Observable<EntityResponseType> {
    return this.http.put<IRecurringTransaction>(this.resourceUrl, recurringTransaction, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRecurringTransaction>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRecurringTransaction[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
