import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AccountingSharedModule } from 'app/shared/shared.module';
import { LedgerComponent } from './ledger.component';
import { LedgerDetailComponent } from './ledger-detail.component';
import { LedgerUpdateComponent } from './ledger-update.component';
import { LedgerDeleteDialogComponent } from './ledger-delete-dialog.component';
import { ledgerRoute } from './ledger.route';

@NgModule({
  imports: [AccountingSharedModule, RouterModule.forChild(ledgerRoute)],
  declarations: [LedgerComponent, LedgerDetailComponent, LedgerUpdateComponent, LedgerDeleteDialogComponent],
  entryComponents: [LedgerDeleteDialogComponent]
})
export class AccountingLedgerModule {}
