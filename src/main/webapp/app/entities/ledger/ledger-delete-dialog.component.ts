import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILedger } from 'app/shared/model/ledger.model';
import { LedgerService } from './ledger.service';

@Component({
  templateUrl: './ledger-delete-dialog.component.html'
})
export class LedgerDeleteDialogComponent {
  ledger?: ILedger;

  constructor(protected ledgerService: LedgerService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ledgerService.delete(id).subscribe(() => {
      this.eventManager.broadcast('ledgerListModification');
      this.activeModal.close();
    });
  }
}
