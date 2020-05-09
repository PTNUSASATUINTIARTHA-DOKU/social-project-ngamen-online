import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrganizer } from 'app/shared/model/organizer.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { OrganizerService } from './organizer.service';
import { OrganizerDeleteDialogComponent } from './organizer-delete-dialog.component';

@Component({
  selector: 'jhi-organizer',
  templateUrl: './organizer.component.html'
})
export class OrganizerComponent implements OnInit, OnDestroy {
  organizers: IOrganizer[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected organizerService: OrganizerService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.organizers = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.organizerService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IOrganizer[]>) => this.paginateOrganizers(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.organizers = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInOrganizers();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IOrganizer): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInOrganizers(): void {
    this.eventSubscriber = this.eventManager.subscribe('organizerListModification', () => this.reset());
  }

  delete(organizer: IOrganizer): void {
    const modalRef = this.modalService.open(OrganizerDeleteDialogComponent, {
      windowClass: 'custom-modal',
      backdropClass: 'fixed top-0 left-0 w-screen h-screen bg-gray-900 opacity-50'
    });
    modalRef.componentInstance.organizer = organizer;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateOrganizers(data: IOrganizer[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.organizers.push(data[i]);
      }
    }
  }
}
