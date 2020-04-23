import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { DonationService } from 'app/entities/donation/donation.service';
import { IPaymentDTO } from 'app/shared/model/dto/payment-dto.model';
import { PaymentChannel } from 'app/shared/model/enumerations/payment-channel.model';

@Component({
  selector: 'jhi-main',
  templateUrl: './payment.component.html',
  styleUrls: ['payment.scss']
})
export class PaymentComponent implements OnInit, OnDestroy {
  payment: IPaymentDTO;
  slug: string;
  method: PaymentChannel;
  isSaving = false;

  paymentForm = this.fb.group({
    donor: [null, [Validators.required, Validators.maxLength(30)]],
    donorAnon: [null, []],
    amount: [null, [Validators.required, Validators.min(10000)]],
    phone: [null, [Validators.required, Validators.maxLength(13), Validators.minLength(10)]]
  });

  constructor(protected service: DonationService, protected route: ActivatedRoute, private fb: FormBuilder) {
    this.slug = '';
    this.payment = {};
    this.method = PaymentChannel.OVO;
  }

  ngOnInit(): void {
    // to access resolved item = this.route.snapshot.data.payment check using console.log(this.route); with comment eslint-disable-next-line no-console
    this.payment = this.route.snapshot.data.payment;
    this.slug = this.route.snapshot.params.slug;
    this.paymentForm.patchValue({ phone: '08' });
  }

  anon(): void {
    if (this.paymentForm.get('donorAnon')?.value) {
      // eslint-disable-next-line no-console
      console.log('=============== ANON TRUE');
      this.paymentForm.get('donor')?.clearValidators();
      this.paymentForm.get('donor')?.disable();
    } else {
      // eslint-disable-next-line no-console
      console.log('=============== ANON FALSE');
      this.paymentForm.get('donor')?.enable();
      this.paymentForm.get('donor')?.setValidators([Validators.required, Validators.maxLength(30)]);
    }
  }

  ngOnDestroy(): void {}

  save(): void {
    this.isSaving = true;
    // const payment = this.createFromForm();
    // this.subscribeToSaveResponse(this.donationService.update(donation));
  }

  private createFromForm(): IPaymentDTO {
    return this.payment;
  }
}
