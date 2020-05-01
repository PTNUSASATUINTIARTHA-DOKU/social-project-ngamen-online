import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { DonationService } from 'app/entities/donation/donation.service';
import { PaymentChannel } from 'app/shared/model/enumerations/payment-channel.model';
import { Transaction, ITransaction } from 'app/shared/model/transaction.model';
import { DeviceDetectorService } from 'ngx-device-detector';
import { Observable, timer } from 'rxjs';
import { map, take } from 'rxjs/operators';
import { v4 as uuid } from 'uuid';
import { PaymentService } from './payment.service';
import { Donation } from 'app/shared/model/donation.model';
import { TransactionStatus } from 'app/shared/model/enumerations/transaction-status.model';

@Component({
  selector: 'jhi-main',
  templateUrl: './payment.component.html',
  styleUrls: ['payment.scss']
})
export class PaymentComponent implements OnInit {
  slug: string;
  method: PaymentChannel;
  isSaving = false;
  isCounting = false;
  counter$: Observable<number> = new Observable<number>();
  count = 0;
  donation: Donation;
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
    private fb: FormBuilder,
    private device: DeviceDetectorService,
    private router: Router
  ) {
    this.slug = '';
    this.donation = {};
    this.transaction = {};
    this.method = PaymentChannel.OVO;
  }

  ngOnInit(): void {
    // to access resolved item = this.route.snapshot.data.payment check using console.log(this.route); with comment eslint-disable-next-line no-console
    this.donation = this.route.snapshot.data.donation;
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

  confirm(): void {
    this.isCounting = true;
    this.count = 30;
    this.counter$ = timer(0, 1000).pipe(
      take(this.count),
      map(() => --this.count)
    );
    const transaction = this.createPayment();
    this.paymentService.initPayment(transaction).subscribe(
      result => {
        if (result.body) this.onSaveSuccess(result.body);
        else this.onSaveError();
      },
      () => this.onSaveError()
    );
  }

  save(): void {
    this.isSaving = true;
  }

  private createPayment(): ITransaction {
    const uuids = uuid();
    this.transaction = new Transaction(
      undefined,
      uuids,
      uuids,
      this.generateBasket(),
      undefined,
      this.generateDeviceId(),
      this.paymentForm.get('donor')?.value,
      this.paymentForm.get('phone')?.value,
      undefined,
      this.paymentForm.get('amount')?.value,
      this.method,
      undefined,
      undefined,
      undefined,
      undefined,
      undefined,
      undefined,
      undefined,
      undefined,
      undefined,
      undefined,
      TransactionStatus.FAILED,
      this.donation
    );
    return this.transaction;
  }

  private generateBasket(): string {
    const amt = this.paymentForm.get('amount')?.value;
    return this.donation?.name + ' by ' + this.donation?.organizer?.name + ', ' + amt + ', 1, ' + amt;
  }

  private generateDeviceId(): string {
    return JSON.stringify(this.device.getDeviceInfo());
  }

  private onSaveSuccess(response: ITransaction): void {
    this.isSaving = false;
    this.paymentService.paymentResult(response);
    this.router.navigate(['result'], { relativeTo: this.route });
  }

  private onSaveError(): void {
    this.isSaving = false;
  }
}
