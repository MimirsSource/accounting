import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILedger } from 'app/shared/model/ledger.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { LedgerService } from './ledger.service';
import { LedgerDeleteDialogComponent } from './ledger-delete-dialog.component';

@Component({
  selector: 'jhi-ledger',
  templateUrl: './ledger.component.html'
})
export class LedgerComponent implements OnInit, OnDestroy {
  ledgers: ILedger[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected ledgerService: LedgerService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.ledgers = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.ledgerService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<ILedger[]>) => this.paginateLedgers(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.ledgers = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInLedgers();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ILedger): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInLedgers(): void {
    this.eventSubscriber = this.eventManager.subscribe('ledgerListModification', () => this.reset());
  }

  delete(ledger: ILedger): void {
    const modalRef = this.modalService.open(LedgerDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.ledger = ledger;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateLedgers(data: ILedger[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.ledgers.push(data[i]);
      }
    }
  }
}
