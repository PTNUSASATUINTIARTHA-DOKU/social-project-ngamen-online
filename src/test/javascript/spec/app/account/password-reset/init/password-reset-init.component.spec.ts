import { ComponentFixture, inject, TestBed } from '@angular/core/testing';
import { FormBuilder } from '@angular/forms';
import { PasswordResetInitComponent } from 'app/account/password-reset/init/password-reset-init.component';
import { PasswordResetInitService } from 'app/account/password-reset/init/password-reset-init.service';
import { of, throwError } from 'rxjs';
import { DonationTestModule } from '../../../../test.module';

describe('Component Tests', () => {
  describe('PasswordResetInitComponent', () => {
    let fixture: ComponentFixture<PasswordResetInitComponent>;
    let comp: PasswordResetInitComponent;

    beforeEach(() => {
      fixture = TestBed.configureTestingModule({
        imports: [DonationTestModule],
        declarations: [PasswordResetInitComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PasswordResetInitComponent, '')
        .createComponent(PasswordResetInitComponent);
      comp = fixture.componentInstance;
    });

    it('notifies of success upon successful requestReset', inject([PasswordResetInitService], (service: PasswordResetInitService) => {
      spyOn(service, 'save').and.returnValue(of({}));
      comp.resetRequestForm.patchValue({
        email: 'user@domain.com',
        captchaToken: 'token'
      });

      comp.requestReset();

      expect(service.save).toHaveBeenCalledWith('user@domain.com', '');
      expect(comp.success).toBe(true);
    }));

    it('no notification of success upon error response', inject([PasswordResetInitService], (service: PasswordResetInitService) => {
      spyOn(service, 'save').and.returnValue(
        throwError({
          status: 503,
          data: 'something else'
        })
      );
      comp.resetRequestForm.patchValue({
        email: 'user@domain.com'
      });
      comp.requestReset();

      expect(service.save).toHaveBeenCalledWith('user@domain.com', '');
      expect(comp.success).toBe(false);
    }));
  });
});
