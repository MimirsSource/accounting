import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRecurringTransaction } from 'app/shared/model/recurring-transaction.model';
import { RecurringTransactionService } from './recurring-transaction.service';

@Component({
  templateUrl: './recurring-transaction-delete-dialog.component.html'
})
export class RecurringTransactionDeleteDialogComponent {
  recurringTransaction?: IRecurringTransaction;

  constructor(
    protected recurringTransactionService: RecurringTransactionService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.recurringTransactionService.delete(id).subscribe(() => {
      this.eventManager.broadcast('recurringTransactionListModification');
      this.activeModal.close();
    });
  }
}
