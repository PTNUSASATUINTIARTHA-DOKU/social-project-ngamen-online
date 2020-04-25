import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PaymentDTO } from 'app/shared/model/dto/payment-dto.model';

@Component({
  selector: 'jhi-main',
  templateUrl: './payment.result.component.html',
  styleUrls: ['payment.scss']
})
export class PaymentResultComponent implements OnInit {
  payment: PaymentDTO;
  message = '';

  constructor(protected route: ActivatedRoute) {
    this.payment = {};
  }

  ngOnInit(): void {
    this.payment = this.route.snapshot.data.payment;
    this.message = JSON.stringify(this.payment);
  }
}
