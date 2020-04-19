import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { DonationTestModule } from '../../../test.module';
import { OrganizerUpdateComponent } from 'app/entities/organizer/organizer-update.component';
import { OrganizerService } from 'app/entities/organizer/organizer.service';
import { Organizer } from 'app/shared/model/organizer.model';

describe('Component Tests', () => {
  describe('Organizer Management Update Component', () => {
    let comp: OrganizerUpdateComponent;
    let fixture: ComponentFixture<OrganizerUpdateComponent>;
    let service: OrganizerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DonationTestModule],
        declarations: [OrganizerUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(OrganizerUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OrganizerUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(OrganizerService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Organizer(123);
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
        const entity = new Organizer();
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
