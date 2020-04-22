import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DonationService } from 'app/entities/donation/donation.service';
import { IPaymentDTO } from 'app/shared/model/dto/payment-dto.model';

@Component({
  selector: 'jhi-main',
  templateUrl: './payment.component.html',
  styleUrls: ['payment.scss']
})
export class PaymentComponent implements OnInit, OnDestroy {
  payment: IPaymentDTO;
  slug: string;

  constructor(protected service: DonationService, protected route: ActivatedRoute) {
    this.slug = '';
    this.payment = {};
  }

  ngOnInit(): void {
    // to access resolved item = this.route.snapshot.data.payment check using console.log(this.route); with comment eslint-disable-next-line no-console
    this.payment = this.route.snapshot.data.payment;
    this.slug = this.route.snapshot.params.slug;
  }

  ngOnDestroy(): void {}
}
