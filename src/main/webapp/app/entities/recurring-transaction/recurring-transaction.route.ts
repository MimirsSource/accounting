import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IRecurringTransaction, RecurringTransaction } from 'app/shared/model/recurring-transaction.model';
import { RecurringTransactionService } from './recurring-transaction.service';
import { RecurringTransactionComponent } from './recurring-transaction.component';
import { RecurringTransactionDetailComponent } from './recurring-transaction-detail.component';
import { RecurringTransactionUpdateComponent } from './recurring-transaction-update.component';

@Injectable({ providedIn: 'root' })
export class RecurringTransactionResolve implements Resolve<IRecurringTransaction> {
  constructor(private service: RecurringTransactionService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRecurringTransaction> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((recurringTransaction: HttpResponse<RecurringTransaction>) => {
          if (recurringTransaction.body) {
            return of(recurringTransaction.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RecurringTransaction());
  }
}

export const recurringTransactionRoute: Routes = [
  {
    path: '',
    component: RecurringTransactionComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'accountingApp.recurringTransaction.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: RecurringTransactionDetailComponent,
    resolve: {
      recurringTransaction: RecurringTransactionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'accountingApp.recurringTransaction.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: RecurringTransactionUpdateComponent,
    resolve: {
      recurringTransaction: RecurringTransactionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'accountingApp.recurringTransaction.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: RecurringTransactionUpdateComponent,
    resolve: {
      recurringTransaction: RecurringTransactionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'accountingApp.recurringTransaction.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
