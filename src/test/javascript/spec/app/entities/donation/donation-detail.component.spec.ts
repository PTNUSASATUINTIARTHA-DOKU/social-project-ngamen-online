import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DonationTestModule } from '../../../test.module';
import { DonationDetailComponent } from 'app/entities/donation/donation-detail.component';
import { Donation } from 'app/shared/model/donation.model';

describe('Component Tests', () => {
  describe('Donation Management Detail Component', () => {
    let comp: DonationDetailComponent;
    let fixture: ComponentFixture<DonationDetailComponent>;
    const route = ({ data: of({ donation: new Donation(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DonationTestModule],
        declarations: [DonationDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(DonationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DonationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load donation on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.donation).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
