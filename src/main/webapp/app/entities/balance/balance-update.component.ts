import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IBalance, Balance } from 'app/shared/model/balance.model';
import { BalanceService } from './balance.service';
import { ILedger } from 'app/shared/model/ledger.model';
import { LedgerService } from 'app/entities/ledger/ledger.service';

@Component({
  selector: 'jhi-balance-update',
  templateUrl: './balance-update.component.html'
})
export class BalanceUpdateComponent implements OnInit {
  isSaving = false;

  ledgers: ILedger[] = [];

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]],
    amount: [null, [Validators.required]],
    ledger: []
  });

  constructor(
    protected balanceService: BalanceService,
    protected ledgerService: LedgerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ balance }) => {
      this.updateForm(balance);

      this.ledgerService
        .query({ filter: 'balance-is-null' })
        .pipe(
          map((res: HttpResponse<ILedger[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ILedger[]) => {
          if (!balance.ledger || !balance.ledger.id) {
            this.ledgers = resBody;
          } else {
            this.ledgerService
              .find(balance.ledger.id)
              .pipe(
                map((subRes: HttpResponse<ILedger>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ILedger[]) => {
                this.ledgers = concatRes;
              });
          }
        });
    });
  }

  updateForm(balance: IBalance): void {
    this.editForm.patchValue({
      id: balance.id,
      date: balance.date != null ? balance.date.format(DATE_TIME_FORMAT) : null,
      amount: balance.amount,
      ledger: balance.ledger
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const balance = this.createFromForm();
    if (balance.id !== undefined) {
      this.subscribeToSaveResponse(this.balanceService.update(balance));
    } else {
      this.subscribeToSaveResponse(this.balanceService.create(balance));
    }
  }

  private createFromForm(): IBalance {
    return {
      ...new Balance(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value != null ? moment(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      amount: this.editForm.get(['amount'])!.value,
      ledger: this.editForm.get(['ledger'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBalance>>): void {
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
