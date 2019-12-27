import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IRecurringTransaction, RecurringTransaction } from 'app/shared/model/recurring-transaction.model';
import { RecurringTransactionService } from './recurring-transaction.service';
import { ILedger } from 'app/shared/model/ledger.model';
import { LedgerService } from 'app/entities/ledger/ledger.service';

@Component({
  selector: 'jhi-recurring-transaction-update',
  templateUrl: './recurring-transaction-update.component.html'
})
export class RecurringTransactionUpdateComponent implements OnInit {
  isSaving = false;

  fromledgers: ILedger[] = [];

  toledgers: ILedger[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    amount: [null, [Validators.required]],
    dateOfMonth: [null, [Validators.required]],
    fromLedger: [],
    toLedger: []
  });

  constructor(
    protected recurringTransactionService: RecurringTransactionService,
    protected ledgerService: LedgerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recurringTransaction }) => {
      this.updateForm(recurringTransaction);

      this.ledgerService
        .query({ filter: 'recurringtransaction-is-null' })
        .pipe(
          map((res: HttpResponse<ILedger[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ILedger[]) => {
          if (!recurringTransaction.fromLedger || !recurringTransaction.fromLedger.id) {
            this.fromledgers = resBody;
          } else {
            this.ledgerService
              .find(recurringTransaction.fromLedger.id)
              .pipe(
                map((subRes: HttpResponse<ILedger>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ILedger[]) => {
                this.fromledgers = concatRes;
              });
          }
        });

      this.ledgerService
        .query({ filter: 'recurringtransaction-is-null' })
        .pipe(
          map((res: HttpResponse<ILedger[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ILedger[]) => {
          if (!recurringTransaction.toLedger || !recurringTransaction.toLedger.id) {
            this.toledgers = resBody;
          } else {
            this.ledgerService
              .find(recurringTransaction.toLedger.id)
              .pipe(
                map((subRes: HttpResponse<ILedger>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ILedger[]) => {
                this.toledgers = concatRes;
              });
          }
        });
    });
  }

  updateForm(recurringTransaction: IRecurringTransaction): void {
    this.editForm.patchValue({
      id: recurringTransaction.id,
      name: recurringTransaction.name,
      amount: recurringTransaction.amount,
      dateOfMonth: recurringTransaction.dateOfMonth,
      fromLedger: recurringTransaction.fromLedger,
      toLedger: recurringTransaction.toLedger
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const recurringTransaction = this.createFromForm();
    if (recurringTransaction.id !== undefined) {
      this.subscribeToSaveResponse(this.recurringTransactionService.update(recurringTransaction));
    } else {
      this.subscribeToSaveResponse(this.recurringTransactionService.create(recurringTransaction));
    }
  }

  private createFromForm(): IRecurringTransaction {
    return {
      ...new RecurringTransaction(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      dateOfMonth: this.editForm.get(['dateOfMonth'])!.value,
      fromLedger: this.editForm.get(['fromLedger'])!.value,
      toLedger: this.editForm.get(['toLedger'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRecurringTransaction>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: ILedger): any {
    return item.id;
  }
}
