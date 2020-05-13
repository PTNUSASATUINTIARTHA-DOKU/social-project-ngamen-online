import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDonation, Donation } from 'app/shared/model/donation.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { DonationService } from './donation.service';
import { DonationDeleteDialogComponent } from './donation-delete-dialog.component';
import { PAYMENT_URL_PREFIX } from 'app/shared/constants/path.constants';

@Component({
  selector: 'jhi-donation',
  templateUrl: './donation.component.html'
})
export class DonationComponent implements OnInit, OnDestroy {
  donations: IDonation[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected donationService: DonationService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.donations = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.donationService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IDonation[]>) => this.paginateDonations(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.donations = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInDonations();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IDonation): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInDonations(): void {
    this.eventSubscriber = this.eventManager.subscribe('donationListModification', () => this.reset());
  }

  delete(donation: IDonation): void {
    const modalRef = this.modalService.open(DonationDeleteDialogComponent, {
      windowClass: 'custom-modal',
      backdropClass: 'fixed top-0 left-0 w-screen h-screen bg-gray-900 opacity-50'
    });
    modalRef.componentInstance.donation = donation;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateDonations(data: IDonation[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        (data[i] as Donation).paymentSlug = PAYMENT_URL_PREFIX + (data[i] as Donation).paymentSlug;
        this.donations.push(data[i]);
      }
    }
  }
}
