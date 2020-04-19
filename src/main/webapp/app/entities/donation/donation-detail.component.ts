import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDonation } from 'app/shared/model/donation.model';

@Component({
  selector: 'jhi-donation-detail',
  templateUrl: './donation-detail.component.html'
})
export class DonationDetailComponent implements OnInit {
  donation: IDonation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ donation }) => (this.donation = donation));
  }

  previousState(): void {
    window.history.back();
  }
}
