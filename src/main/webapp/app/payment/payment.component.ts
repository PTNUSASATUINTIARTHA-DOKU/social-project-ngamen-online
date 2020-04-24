import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { DonationService } from 'app/entities/donation/donation.service';
import { IPaymentDTO } from 'app/shared/model/dto/payment-dto.model';
import { PaymentChannel } from 'app/shared/model/enumerations/payment-channel.model';
import { Transaction } from 'app/shared/model/transaction.model';
import { Observable, timer } from 'rxjs';
import { map, take } from 'rxjs/operators';
import { PaymentService } from './payment.service';

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
  counter$: Observable<number> = new Observable<number>();
  count = 30;
  transaction: Transaction;

  paymentForm = this.fb.group({
    donor: [null, [Validators.required, Validators.maxLength(30)]],
    donorAnon: [null, []],
    amount: [null, [Validators.required, Validators.min(10000)]],
    phone: [null, [Validators.required, Validators.maxLength(13), Validators.minLength(10)]]
  });

  constructor(
    protected service: DonationService,
    protected paymentService: PaymentService,
    protected route: ActivatedRoute,
    private fb: FormBuilder
  ) {
    this.slug = '';
    this.payment = {};
    this.transaction = {};
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
      this.paymentForm.get('donor')?.clearValidators();
      this.paymentForm.get('donor')?.setValue('');
      this.paymentForm.get('donor')?.disable();
    } else {
      this.paymentForm.get('donor')?.enable();
      this.paymentForm.get('donor')?.setValidators([Validators.required, Validators.maxLength(30)]);
    }
  }

  ngOnDestroy(): void {}

  save(): void {
    this.isSaving = true;
    const paymentDTO = this.createPayment();
    this.counter$ = timer(0, 1000).pipe(
      take(this.count),
      map(() => --this.count)
    );
    setTimeout(() => {
      this.paymentService.initPayment(paymentDTO).subscribe(
        () => this.onSaveSuccess(),
        () => this.onSaveError()
      );
    }, 30000);
  }

  private createPayment(): IPaymentDTO {
    this.transaction = new Transaction(
      undefined,
      undefined,
      undefined,
      undefined,
      undefined,
      undefined,
      this.paymentForm.get('donor')?.value,
      this.paymentForm.get('phone')?.value,
      undefined,
      this.paymentForm.get('amount')?.value,
      this.method,
      undefined,
      undefined,
      undefined,
      undefined
    );
    return this.payment;
  }

  private onSaveSuccess(): void {
    this.isSaving = false;
    // go to result page
  }

  private onSaveError(): void {
    this.isSaving = false;
    // show donation page + error message
  }
}
