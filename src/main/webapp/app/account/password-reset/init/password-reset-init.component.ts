import { Component, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';

import { PasswordResetInitService } from './password-reset-init.service';

declare let grecaptcha: any;

@Component({
  selector: 'jhi-password-reset-init',
  templateUrl: './password-reset-init.component.html'
})
export class PasswordResetInitComponent implements AfterViewInit {
  @ViewChild('email', { static: false })
  email?: ElementRef;

  success = false;
  resetRequestForm = this.fb.group({
    email: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    greToken: ['', [Validators.required]]
  });

  constructor(private passwordResetInitService: PasswordResetInitService, private fb: FormBuilder) {}

  ngAfterViewInit(): void {
    if (this.email) {
      this.email.nativeElement.focus();
    }
    this.checkRecaptcha(this);
  }

  checkRecaptcha(reset: PasswordResetInitComponent): void {
    grecaptcha.ready(function(): void {
      grecaptcha.execute('6LdMRfgUAAAAAM90L4mxhYqhnAuxzEerTbQFRUYq', { action: 'reset_password' }).then(function(token: any): void {
        reset.resetRequestForm.patchValue({ greToken: token });
      });
    });
  }

  requestReset(): void {
    this.passwordResetInitService
      .save(this.resetRequestForm.get(['email'])!.value, this.resetRequestForm.get(['greToken'])!.value)
      .subscribe(() => (this.success = true));
  }
}
