import { Component, OnInit, HostListener } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { DonationService } from 'app/entities/donation/donation.service';
import { PaymentChannel } from 'app/shared/model/enumerations/payment-channel.model';
import { Transaction, ITransaction } from 'app/shared/model/transaction.model';
import { DeviceDetectorService } from 'ngx-device-detector';
import { Observable, timer } from 'rxjs';
import { map, take, finalize } from 'rxjs/operators';
import { v4 as uuid } from 'uuid';
import { PaymentService } from './payment.service';
import { Donation } from 'app/shared/model/donation.model';
import { TransactionStatus } from 'app/shared/model/enumerations/transaction-status.model';
import * as moment from 'moment';
import { PATTERN_WITHOUT_SYMBOL, PATTERN_MOBILE_PHONE } from 'app/shared/constants/pattern.constants';
import { IsActiveStatus } from 'app/shared/model/enumerations/is-active-status.model';
import * as crypto from 'crypto-js';

declare let dataLayer: any[];
declare let grecaptcha: any;

@Component({
  selector: 'jhi-main',
  templateUrl: './payment.offline.component.html',
  styleUrls: ['payment.scss']
})
export class PaymentOfflineComponent implements OnInit {
  slug: string;
  method: PaymentChannel;
  isDisabled = false;
  isSaving = false;
  isCounting = false;
  isChecking = false;
  counter$: Observable<number> = new Observable<number>();
  count = 0;
  donation: Donation;
  transaction: Transaction;
  windowScrolled: boolean | undefined;
  startTime: number;
  isLogin = false;
  isLogout = false;

  covidForm = this.fb.group({
    contactName: [null, { validators: [Validators.required, Validators.maxLength(30)] }],
    contactPhone: [
      null,
      { validators: [Validators.required, Validators.maxLength(13), Validators.minLength(10), Validators.pattern(PATTERN_MOBILE_PHONE)] }
    ]
  });

  paymentForm = this.fb.group({
    amount: [null, { validators: [Validators.required, Validators.min(10000), Validators.max(1000000000)] }],
    greToken: [null, [Validators.required]],
    mallId: [],
    chainMerchant: [],
    purchaseAmount: [],
    transIdMerchant: [],
    paymentType: [],
    words: [],
    requestDateTime: [],
    currency: [],
    purchaseCurrency: [],
    sessionId: [],
    name: [],
    email: [],
    basket: []
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
    this.startTime = new Date().getTime();
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
    if (this.donation.status === IsActiveStatus.DISABLED) {
      this.isDisabled = true;
      this.paymentForm.disable();
    }
    this.slug = this.route.snapshot.params.slug;
    this.covidForm.patchValue({ contactPhone: '08' });
    this.checkRecaptcha(this);
  }

  checkRecaptcha(paymentComponent: PaymentOfflineComponent): void {
    grecaptcha.ready(function(): void {
      grecaptcha.execute('6LdMRfgUAAAAAM90L4mxhYqhnAuxzEerTbQFRUYq', { action: 'payment' }).then(function(token: any): void {
        paymentComponent.paymentForm.patchValue({ greToken: token });
      });
    });
  }

  checkIn(): void {
    this.isLogin = true;
  }

  checkOut(): void {
    this.isLogout = true;
  }

  generateWords(): void {
    const amountStr = this.paymentForm.get('amount')?.value + '.00';
    const now = moment(new Date()).format('YYYYMMDDhhmmss');
    const basket1 = 'basket,' + amountStr + ',1,' + amountStr;
    const sha1words = crypto.SHA1(amountStr + '11580240' + 'DDWb9fRV585Q' + now);
    this.paymentForm.patchValue({ mallId: '11580240' });
    this.paymentForm.patchValue({ chainMerchant: 'NA' });
    this.paymentForm.patchValue({ paymentType: 'SALE' });
    this.paymentForm.patchValue({ currency: '360' });
    this.paymentForm.patchValue({ purchaseCurrency: '360' });
    this.paymentForm.patchValue({ name: 'Test Customer' });
    this.paymentForm.patchValue({ email: 'customer@doku.com' });
    this.paymentForm.patchValue({ amount: amountStr });
    this.paymentForm.patchValue({ purchaseAmount: amountStr });
    this.paymentForm.patchValue({ transIdMerchant: now });
    this.paymentForm.patchValue({ requestDateTime: now });
    this.paymentForm.patchValue({ sessionId: now });
    this.paymentForm.patchValue({ basket: basket1 });
    this.paymentForm.patchValue({ words: sha1words });
    console.warn(this.paymentForm);
  }

  save(): void {
    const submitTime = new Date().getTime();
    dataLayer.push({
      event: 'Finish Form Payment',
      timeToFinishPaymentForm: submitTime - this.startTime
    });
    const timeCouting = 30;
    const timeChecking = 40;
    this.isSaving = true;
    this.isChecking = false;
    this.isCounting = true;
    this.createPayment();
    const sub = this.paymentService
      .initPayment(this.transaction)
      .pipe(finalize(() => this.finishPaymentOvo(new Date().getTime() - submitTime)))
      .subscribe(
        result => {
          if (result.body) {
            this.transaction = result.body;
            this.onSaveSuccess(this.transaction);
          } else {
            this.onSaveError(this.transaction);
          }
        },
        () => this.onSaveError(this.transaction)
      );
    this.count = timeCouting;
    this.counter$ = timer(0, 1000).pipe(
      take(timeChecking),
      map(() => {
        --this.count;
        if (this.count === 0) {
          this.isCounting = false;
          if (!this.isChecking) {
            this.count = timeChecking;
            this.isChecking = true;
          } else {
            this.isChecking = false;
            this.isSaving = false;
            this.finishPaymentOvo(70000);
            sub.unsubscribe();
            this.onSaveError(this.transaction);
          }
        }
        return this.count;
      })
    );
  }

  private createPayment(): void {
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
      this.paymentForm.get('greToken')?.value,
      undefined,
      undefined,
      undefined,
      TransactionStatus.FAILED,
      this.donation
    );
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

  private finishPaymentOvo(time: number): void {
    dataLayer.push({
      event: 'Finish Payment OVO',
      timeToFinishPayment: time,
      donationAmount: this.transaction.amount,
      transactionStatus: this.transaction.status
    });
  }
}
