import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ITransaction, Transaction } from 'app/shared/model/transaction.model';
import { TransactionService } from './transaction.service';
import { ILedger } from 'app/shared/model/ledger.model';
import { LedgerService } from 'app/entities/ledger/ledger.service';
import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category/category.service';

type SelectableEntity = ILedger | ICategory;

@Component({
  selector: 'jhi-transaction-update',
  templateUrl: './transaction-update.component.html'
})
export class TransactionUpdateComponent implements OnInit {
  isSaving = false;

  fromledgers: ILedger[] = [];

  toledgers: ILedger[] = [];

  categories: ICategory[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    amount: [null, [Validators.required]],
    date: [null, [Validators.required]],
    initial: [null, [Validators.required]],
    fromLedger: [],
    toLedger: [],
    category: []
  });

  constructor(
    protected transactionService: TransactionService,
    protected ledgerService: LedgerService,
    protected categoryService: CategoryService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transaction }) => {
      this.updateForm(transaction);

      this.ledgerService
        .query({ filter: 'transaction-is-null' })
        .pipe(
          map((res: HttpResponse<ILedger[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ILedger[]) => {
          if (!transaction.fromLedger || !transaction.fromLedger.id) {
            this.fromledgers = resBody;
          } else {
            this.ledgerService
              .find(transaction.fromLedger.id)
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
        .query({ filter: 'transaction-is-null' })
        .pipe(
          map((res: HttpResponse<ILedger[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ILedger[]) => {
          if (!transaction.toLedger || !transaction.toLedger.id) {
            this.toledgers = resBody;
          } else {
            this.ledgerService
              .find(transaction.toLedger.id)
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

      this.categoryService
        .query()
        .pipe(
          map((res: HttpResponse<ICategory[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ICategory[]) => (this.categories = resBody));
    });
  }

  updateForm(transaction: ITransaction): void {
    this.editForm.patchValue({
      id: transaction.id,
      name: transaction.name,
      amount: transaction.amount,
      date: transaction.date != null ? transaction.date.format(DATE_TIME_FORMAT) : null,
      initial: transaction.initial,
      fromLedger: transaction.fromLedger,
      toLedger: transaction.toLedger,
      category: transaction.category
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const transaction = this.createFromForm();
    if (transaction.id !== undefined) {
      this.subscribeToSaveResponse(this.transactionService.update(transaction));
    } else {
      this.subscribeToSaveResponse(this.transactionService.create(transaction));
    }
  }

  private createFromForm(): ITransaction {
    return {
      ...new Transaction(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      date: this.editForm.get(['date'])!.value != null ? moment(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      initial: this.editForm.get(['initial'])!.value,
      fromLedger: this.editForm.get(['fromLedger'])!.value,
      toLedger: this.editForm.get(['toLedger'])!.value,
      category: this.editForm.get(['category'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransaction>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
