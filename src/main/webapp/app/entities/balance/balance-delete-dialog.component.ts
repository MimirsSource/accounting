import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBalance } from 'app/shared/model/balance.model';
import { BalanceService } from './balance.service';

@Component({
  templateUrl: './balance-delete-dialog.component.html'
})
export class BalanceDeleteDialogComponent {
  balance?: IBalance;

  constructor(protected balanceService: BalanceService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.balanceService.delete(id).subscribe(() => {
      this.eventManager.broadcast('balanceListModification');
      this.activeModal.close();
    });
  }
}
