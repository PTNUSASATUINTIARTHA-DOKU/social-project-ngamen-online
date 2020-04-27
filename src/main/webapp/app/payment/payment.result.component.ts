import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PaymentDTO } from 'app/shared/model/dto/payment-dto.model';
import { PAYMENT_URL_PREFIX } from 'app/shared/constants/path.constants';

@Component({
  selector: 'jhi-main',
  templateUrl: './payment.result.component.html',
  styleUrls: ['payment.scss']
})
export class PaymentResultComponent implements OnInit {
  payment: PaymentDTO;
  isSuccess = false;
  paymentUrl = '';

  constructor(protected route: ActivatedRoute) {
    this.payment = {};
  }

  ngOnInit(): void {
    this.payment = this.route.snapshot.data.payment;
    this.paymentUrl = PAYMENT_URL_PREFIX + this.payment.transaction?.donation?.paymentSlug;
    this.isSuccess = this.payment.transaction?.status === 'SUCCESS';
  }
}
