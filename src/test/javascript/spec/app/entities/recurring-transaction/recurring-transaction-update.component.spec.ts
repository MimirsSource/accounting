import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AccountingTestModule } from '../../../test.module';
import { RecurringTransactionUpdateComponent } from 'app/entities/recurring-transaction/recurring-transaction-update.component';
import { RecurringTransactionService } from 'app/entities/recurring-transaction/recurring-transaction.service';
import { RecurringTransaction } from 'app/shared/model/recurring-transaction.model';

describe('Component Tests', () => {
  describe('RecurringTransaction Management Update Component', () => {
    let comp: RecurringTransactionUpdateComponent;
    let fixture: ComponentFixture<RecurringTransactionUpdateComponent>;
    let service: RecurringTransactionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AccountingTestModule],
        declarations: [RecurringTransactionUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(RecurringTransactionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RecurringTransactionUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RecurringTransactionService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new RecurringTransaction(123);
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
        const entity = new RecurringTransaction();
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
