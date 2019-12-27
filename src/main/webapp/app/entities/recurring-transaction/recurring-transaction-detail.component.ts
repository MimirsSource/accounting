import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRecurringTransaction } from 'app/shared/model/recurring-transaction.model';

@Component({
  selector: 'jhi-recurring-transaction-detail',
  templateUrl: './recurring-transaction-detail.component.html'
})
export class RecurringTransactionDetailComponent implements OnInit {
  recurringTransaction: IRecurringTransaction | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recurringTransaction }) => {
      this.recurringTransaction = recurringTransaction;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
