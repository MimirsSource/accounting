import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AccountingTestModule } from '../../../test.module';
import { LedgerDetailComponent } from 'app/entities/ledger/ledger-detail.component';
import { Ledger } from 'app/shared/model/ledger.model';

describe('Component Tests', () => {
  describe('Ledger Management Detail Component', () => {
    let comp: LedgerDetailComponent;
    let fixture: ComponentFixture<LedgerDetailComponent>;
    const route = ({ data: of({ ledger: new Ledger(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AccountingTestModule],
        declarations: [LedgerDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(LedgerDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LedgerDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load ledger on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ledger).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
