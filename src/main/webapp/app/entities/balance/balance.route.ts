import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IBalance, Balance } from 'app/shared/model/balance.model';
import { BalanceService } from './balance.service';
import { BalanceComponent } from './balance.component';
import { BalanceDetailComponent } from './balance-detail.component';
import { BalanceUpdateComponent } from './balance-update.component';

@Injectable({ providedIn: 'root' })
export class BalanceResolve implements Resolve<IBalance> {
  constructor(private service: BalanceService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBalance> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((balance: HttpResponse<Balance>) => {
          if (balance.body) {
            return of(balance.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Balance());
  }
}

export const balanceRoute: Routes = [
  {
    path: '',
    component: BalanceComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'accountingApp.balance.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: BalanceDetailComponent,
    resolve: {
      balance: BalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'accountingApp.balance.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: BalanceUpdateComponent,
    resolve: {
      balance: BalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'accountingApp.balance.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: BalanceUpdateComponent,
    resolve: {
      balance: BalanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'accountingApp.balance.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
