import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IDonation, Donation } from 'app/shared/model/donation.model';
import { DonationService } from './donation.service';
import { IOrganizer } from 'app/shared/model/organizer.model';
import { OrganizerService } from 'app/entities/organizer/organizer.service';

@Component({
  selector: 'jhi-donation-update',
  templateUrl: './donation-update.component.html'
})
export class DonationUpdateComponent implements OnInit {
  isSaving = false;
  organizers: IOrganizer[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(100)]],
    description: [null, [Validators.maxLength(2000)]],
    url: [null, [Validators.maxLength(100)]],
    imageUrl: [null, [Validators.maxLength(100)]],
    paymentSlug: [null, [Validators.maxLength(100)]],
    bankAccountNumber: [null, [Validators.maxLength(15)]],
    bankAccountName: [null, [Validators.maxLength(100)]],
    bankName: [null, [Validators.maxLength(100)]],
    lastUpdatedBy: [null, [Validators.maxLength(100)]],
    lastUpdatedAt: [],
    status: [],
    organizer: []
  });

  constructor(
    protected donationService: DonationService,
    protected organizerService: OrganizerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ donation }) => {
      if (!donation.id) {
        const today = moment().startOf('day');
        donation.lastUpdatedAt = today;
      }

      this.updateForm(donation);

      this.organizerService.query().subscribe((res: HttpResponse<IOrganizer[]>) => (this.organizers = res.body || []));
    });
  }

  updateForm(donation: IDonation): void {
    this.editForm.patchValue({
      id: donation.id,
      name: donation.name,
      description: donation.description,
      url: donation.url,
      imageUrl: donation.imageUrl,
      paymentSlug: donation.paymentSlug,
      bankAccountNumber: donation.bankAccountNumber,
      bankAccountName: donation.bankAccountName,
      bankName: donation.bankName,
      lastUpdatedBy: donation.lastUpdatedBy,
      lastUpdatedAt: donation.lastUpdatedAt ? donation.lastUpdatedAt.format(DATE_TIME_FORMAT) : null,
      status: donation.status,
      organizer: donation.organizer
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const donation = this.createFromForm();
    if (donation.id !== undefined) {
      this.subscribeToSaveResponse(this.donationService.update(donation));
    } else {
      this.subscribeToSaveResponse(this.donationService.create(donation));
    }
  }

  private createFromForm(): IDonation {
    return {
      ...new Donation(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      url: this.editForm.get(['url'])!.value,
      imageUrl: this.editForm.get(['imageUrl'])!.value,
      paymentSlug: this.editForm.get(['paymentSlug'])!.value,
      bankAccountNumber: this.editForm.get(['bankAccountNumber'])!.value,
      bankAccountName: this.editForm.get(['bankAccountName'])!.value,
      bankName: this.editForm.get(['bankName'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      lastUpdatedAt: this.editForm.get(['lastUpdatedAt'])!.value
        ? moment(this.editForm.get(['lastUpdatedAt'])!.value, DATE_TIME_FORMAT)
        : undefined,
      status: this.editForm.get(['status'])!.value,
      organizer: this.editForm.get(['organizer'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDonation>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IOrganizer): any {
    return item.id;
  }
}
