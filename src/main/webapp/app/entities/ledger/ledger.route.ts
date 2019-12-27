import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ILedger, Ledger } from 'app/shared/model/ledger.model';
import { LedgerService } from './ledger.service';
import { LedgerComponent } from './ledger.component';
import { LedgerDetailComponent } from './ledger-detail.component';
import { LedgerUpdateComponent } from './ledger-update.component';

@Injectable({ providedIn: 'root' })
export class LedgerResolve implements Resolve<ILedger> {
  constructor(private service: LedgerService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILedger> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((ledger: HttpResponse<Ledger>) => {
          if (ledger.body) {
            return of(ledger.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Ledger());
  }
}

export const ledgerRoute: Routes = [
  {
    path: '',
    component: LedgerComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'accountingApp.ledger.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: LedgerDetailComponent,
    resolve: {
      ledger: LedgerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'accountingApp.ledger.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: LedgerUpdateComponent,
    resolve: {
      ledger: LedgerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'accountingApp.ledger.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: LedgerUpdateComponent,
    resolve: {
      ledger: LedgerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'accountingApp.ledger.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
