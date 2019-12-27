import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AccountingTestModule } from '../../../test.module';
import { BalanceUpdateComponent } from 'app/entities/balance/balance-update.component';
import { BalanceService } from 'app/entities/balance/balance.service';
import { Balance } from 'app/shared/model/balance.model';

describe('Component Tests', () => {
  describe('Balance Management Update Component', () => {
    let comp: BalanceUpdateComponent;
    let fixture: ComponentFixture<BalanceUpdateComponent>;
    let service: BalanceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AccountingTestModule],
        declarations: [BalanceUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(BalanceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BalanceUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BalanceService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Balance(123);
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
        const entity = new Balance();
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
