import { Component, OnDestroy, OnInit } from '@angular/core';
import { DonationService } from 'app/entities/donation/donation.service';
import { IDonation } from 'app/shared/model/donation.model';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'jhi-main',
  templateUrl: './payment.component.html',
  styleUrls: ['payment.scss']
})
export class PaymentComponent implements OnInit, OnDestroy {
  donation: IDonation;
  slug: string;

  constructor(protected service: DonationService, protected route: ActivatedRoute) {
    this.slug = '';
    this.donation = {};
  }

  ngOnInit(): void {
    // to access resolved item = this.route.snapshot.data.donation check using console.log(this.route); with comment eslint-disable-next-line no-console
    this.donation = this.route.snapshot.data.donation;
    this.slug = this.route.snapshot.params.slug;
  }

  ngOnDestroy(): void {}
}
