import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AccountingTestModule } from '../../../test.module';
import { RecurringTransactionDetailComponent } from 'app/entities/recurring-transaction/recurring-transaction-detail.component';
import { RecurringTransaction } from 'app/shared/model/recurring-transaction.model';

describe('Component Tests', () => {
  describe('RecurringTransaction Management Detail Component', () => {
    let comp: RecurringTransactionDetailComponent;
    let fixture: ComponentFixture<RecurringTransactionDetailComponent>;
    const route = ({ data: of({ recurringTransaction: new RecurringTransaction(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AccountingTestModule],
        declarations: [RecurringTransactionDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(RecurringTransactionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RecurringTransactionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load recurringTransaction on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.recurringTransaction).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
