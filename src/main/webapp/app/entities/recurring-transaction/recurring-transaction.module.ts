import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AccountingSharedModule } from 'app/shared/shared.module';
import { RecurringTransactionComponent } from './recurring-transaction.component';
import { RecurringTransactionDetailComponent } from './recurring-transaction-detail.component';
import { RecurringTransactionUpdateComponent } from './recurring-transaction-update.component';
import { RecurringTransactionDeleteDialogComponent } from './recurring-transaction-delete-dialog.component';
import { recurringTransactionRoute } from './recurring-transaction.route';

@NgModule({
  imports: [AccountingSharedModule, RouterModule.forChild(recurringTransactionRoute)],
  declarations: [
    RecurringTransactionComponent,
    RecurringTransactionDetailComponent,
    RecurringTransactionUpdateComponent,
    RecurringTransactionDeleteDialogComponent
  ],
  entryComponents: [RecurringTransactionDeleteDialogComponent]
})
export class AccountingRecurringTransactionModule {}
