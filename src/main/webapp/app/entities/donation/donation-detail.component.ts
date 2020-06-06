import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDonation } from 'app/shared/model/donation.model';
import { PAYMENT_URL_PREFIX } from 'app/shared/constants/path.constants';
import { DonationService } from './donation.service';

@Component({
  selector: 'jhi-donation-detail',
  templateUrl: './donation-detail.component.html'
})
export class DonationDetailComponent implements OnInit {
  donation: IDonation | null = null;

  constructor(protected activatedRoute: ActivatedRoute, private donationService: DonationService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ donation }) => {
      donation.paymentSlug = PAYMENT_URL_PREFIX + donation?.paymentSlug;
      this.donation = donation;
    });
  }

  previousState(): void {
    window.history.back();
  }

  sendEmail(id: number): void {
    this.donationService.sendEmail(id).subscribe(
      response => alert(response.body),
      error => alert(error.status)
    );
  }
}
