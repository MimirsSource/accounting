import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ILedger, Ledger } from 'app/shared/model/ledger.model';
import { LedgerService } from './ledger.service';
import { IOwner } from 'app/shared/model/owner.model';
import { OwnerService } from 'app/entities/owner/owner.service';

@Component({
  selector: 'jhi-ledger-update',
  templateUrl: './ledger-update.component.html'
})
export class LedgerUpdateComponent implements OnInit {
  isSaving = false;

  owners: IOwner[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [null, [Validators.required]],
    creationDate: [null, [Validators.required]],
    owner: []
  });

  constructor(
    protected ledgerService: LedgerService,
    protected ownerService: OwnerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ledger }) => {
      this.updateForm(ledger);

      this.ownerService
        .query()
        .pipe(
          map((res: HttpResponse<IOwner[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IOwner[]) => (this.owners = resBody));
    });
  }

  updateForm(ledger: ILedger): void {
    this.editForm.patchValue({
      id: ledger.id,
      name: ledger.name,
      description: ledger.description,
      creationDate: ledger.creationDate != null ? ledger.creationDate.format(DATE_TIME_FORMAT) : null,
      owner: ledger.owner
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ledger = this.createFromForm();
    if (ledger.id !== undefined) {
      this.subscribeToSaveResponse(this.ledgerService.update(ledger));
    } else {
      this.subscribeToSaveResponse(this.ledgerService.create(ledger));
    }
  }

  private createFromForm(): ILedger {
    return {
      ...new Ledger(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      creationDate:
        this.editForm.get(['creationDate'])!.value != null
          ? moment(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
          : undefined,
      owner: this.editForm.get(['owner'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILedger>>): void {
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

  trackById(index: number, item: IOwner): any {
    return item.id;
  }
}
