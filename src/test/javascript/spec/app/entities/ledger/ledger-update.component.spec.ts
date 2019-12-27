import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AccountingTestModule } from '../../../test.module';
import { LedgerUpdateComponent } from 'app/entities/ledger/ledger-update.component';
import { LedgerService } from 'app/entities/ledger/ledger.service';
import { Ledger } from 'app/shared/model/ledger.model';

describe('Component Tests', () => {
  describe('Ledger Management Update Component', () => {
    let comp: LedgerUpdateComponent;
    let fixture: ComponentFixture<LedgerUpdateComponent>;
    let service: LedgerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AccountingTestModule],
        declarations: [LedgerUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(LedgerUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LedgerUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LedgerService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Ledger(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Ledger();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
