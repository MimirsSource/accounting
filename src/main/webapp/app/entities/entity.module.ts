import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'ledger',
        loadChildren: () => import('./ledger/ledger.module').then(m => m.AccountingLedgerModule)
      },
      {
        path: 'transaction',
        loadChildren: () => import('./transaction/transaction.module').then(m => m.AccountingTransactionModule)
      },
      {
        path: 'recurring-transaction',
        loadChildren: () => import('./recurring-transaction/recurring-transaction.module').then(m => m.AccountingRecurringTransactionModule)
      },
      {
        path: 'balance',
        loadChildren: () => import('./balance/balance.module').then(m => m.AccountingBalanceModule)
      },
      {
        path: 'owner',
        loadChildren: () => import('./owner/owner.module').then(m => m.AccountingOwnerModule)
      },
      {
        path: 'category',
        loadChildren: () => import('./category/category.module').then(m => m.AccountingCategoryModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class AccountingEntityModule {}
