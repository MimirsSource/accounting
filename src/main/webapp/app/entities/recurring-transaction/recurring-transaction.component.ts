import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRecurringTransaction } from 'app/shared/model/recurring-transaction.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { RecurringTransactionService } from './recurring-transaction.service';
import { RecurringTransactionDeleteDialogComponent } from './recurring-transaction-delete-dialog.component';

@Component({
  selector: 'jhi-recurring-transaction',
  templateUrl: './recurring-transaction.component.html'
})
export class RecurringTransactionComponent implements OnInit, OnDestroy {
  recurringTransactions: IRecurringTransaction[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected recurringTransactionService: RecurringTransactionService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.recurringTransactions = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.recurringTransactionService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IRecurringTransaction[]>) => this.paginateRecurringTransactions(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.recurringTransactions = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInRecurringTransactions();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IRecurringTransaction): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInRecurringTransactions(): void {
    this.eventSubscriber = this.eventManager.subscribe('recurringTransactionListModification', () => this.reset());
  }

  delete(recurringTransaction: IRecurringTransaction): void {
    const modalRef = this.modalService.open(RecurringTransactionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.recurringTransaction = recurringTransaction;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateRecurringTransactions(data: IRecurringTransaction[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.recurringTransactions.push(data[i]);
      }
    }
  }
}
