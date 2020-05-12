import { Component, OnInit, Inject, HostListener } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { DOCUMENT } from '@angular/common';
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
import * as moment from 'moment';
import { PATTERN_WITHOUT_SYMBOL } from 'app/shared/constants/pattern.constants';

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
  isChecking = false;
  counter$: Observable<number> = new Observable<number>();
  count = 0;
  donation: Donation;
  transaction: Transaction;
  windowScrolled: boolean | undefined;

  paymentForm = this.fb.group({
    donor: [null, [Validators.required, Validators.maxLength(30)]],
    donorAnon: [null, []],
    amount: [null, [Validators.required, Validators.min(10000), Validators.max(10000000)]],
    phone: [null, [Validators.required, Validators.maxLength(13), Validators.minLength(10)]]
  });

  constructor(
    protected service: DonationService,
    protected paymentService: PaymentService,
    protected route: ActivatedRoute,
    private fb: FormBuilder,
    private device: DeviceDetectorService,
    private router: Router,
    @Inject(DOCUMENT) private document: Document
  ) {
    this.slug = '';
    this.donation = {};
    this.transaction = {};
    this.method = PaymentChannel.OVO;
  }

  @HostListener('window:scroll', [])
  onWindowScroll(): void {
    if (window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop > 20) {
      this.windowScrolled = true;
    } else if ((this.windowScrolled && window.pageYOffset) || document.documentElement.scrollTop || document.body.scrollTop < 10) {
      this.windowScrolled = false;
    }
  }

  scrollToTop(): void {
    const currentScroll = document.documentElement.scrollTop || document.body.scrollTop;
    if (currentScroll > 0) {
      window.scrollTo(0, currentScroll - currentScroll / 0);
    }
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
    const transaction = this.createPayment();
    const sub = this.paymentService.initPayment(transaction).subscribe(
      result => {
        if (result.body) this.onSaveSuccess(result.body);
        else this.onSaveError(transaction);
      },
      () => this.onSaveError(transaction)
    );
    this.count = 30;
    this.counter$ = timer(0, 1000).pipe(
      take(40),
      map(() => {
        --this.count;
        if (this.count === 0) {
          this.isCounting = false;
          if (!this.isChecking) {
            this.count = 40;
            this.isChecking = true;
          } else {
            this.isChecking = false;
            this.isSaving = false;
            sub.unsubscribe();
            this.onSaveError(transaction);
          }
        }
        return this.count;
      })
    );
  }

  save(): void {
    this.isSaving = true;
    this.isChecking = false;
    this.isCounting = false;
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
      moment(new Date()),
      'RTO',
      'Failed to communicate with Server',
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
    return (
      this.donation?.name?.replace(PATTERN_WITHOUT_SYMBOL, '') +
      ' by ' +
      this.donation?.organizer?.name?.replace(PATTERN_WITHOUT_SYMBOL, '') +
      ', ' +
      amt +
      ', 1, ' +
      amt
    );
  }

  private generateDeviceId(): string {
    return JSON.stringify(this.device.getDeviceInfo());
  }

  private onSaveSuccess(transaction: ITransaction): void {
    this.isSaving = false;
    this.router.navigate(['../' + transaction.invoiceNumber + '/result'], { relativeTo: this.route });
  }

  private onSaveError(transaction: ITransaction): void {
    this.isSaving = false;
    this.paymentService.paymentResult(transaction);
    this.router.navigate(['../' + transaction.invoiceNumber + '/result'], { relativeTo: this.route });
  }
}
