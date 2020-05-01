import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PAYMENT_URL_PREFIX } from 'app/shared/constants/path.constants';
import { ITransaction } from 'app/shared/model/transaction.model';

@Component({
  selector: 'jhi-main',
  templateUrl: './payment.result.component.html',
  styleUrls: ['payment.scss']
})
export class PaymentResultComponent implements OnInit {
  transaction: ITransaction;
  isSuccess = false;
  paymentUrl = '';

  constructor(protected route: ActivatedRoute) {
    this.transaction = {};
  }

  ngOnInit(): void {
    this.transaction = this.route.snapshot.data.transaction;
    this.paymentUrl = PAYMENT_URL_PREFIX + this.transaction?.donation?.paymentSlug;
    this.isSuccess = this.transaction?.status === 'SUCCESS';
  }
}
