import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IOrganizer, Organizer } from 'app/shared/model/organizer.model';
import { OrganizerService } from './organizer.service';

@Component({
  selector: 'jhi-organizer-update',
  templateUrl: './organizer-update.component.html'
})
export class OrganizerUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(100)]],
    url: [null, [Validators.maxLength(100)]],
    email: [null, [Validators.required, Validators.maxLength(100)]],
    bankAccountNumber: [null, [Validators.maxLength(15)]],
    bankAccountName: [null, [Validators.maxLength(100)]],
    bankName: [null, [Validators.maxLength(100)]],
    mdr: [null, [Validators.max(100)]],
    sharing: [null, [Validators.max(100)]],
    lastUpdatedBy: [null, [Validators.required, Validators.maxLength(100)]],
    lastUpdatedAt: [null, [Validators.required]],
    status: [null, [Validators.required]]
  });

  constructor(protected organizerService: OrganizerService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ organizer }) => {
      if (!organizer.id) {
        const today = moment().startOf('day');
        organizer.lastUpdatedAt = today;
      }

      this.updateForm(organizer);
    });
  }

  updateForm(organizer: IOrganizer): void {
    this.editForm.patchValue({
      id: organizer.id,
      name: organizer.name,
      url: organizer.url,
      email: organizer.email,
      bankAccountNumber: organizer.bankAccountNumber,
      bankAccountName: organizer.bankAccountName,
      bankName: organizer.bankName,
      mdr: organizer.mdr,
      sharing: organizer.sharing,
      lastUpdatedBy: organizer.lastUpdatedBy,
      lastUpdatedAt: organizer.lastUpdatedAt ? organizer.lastUpdatedAt.format(DATE_TIME_FORMAT) : null,
      status: organizer.status
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const organizer = this.createFromForm();
    if (organizer.id !== undefined) {
      this.subscribeToSaveResponse(this.organizerService.update(organizer));
    } else {
      this.subscribeToSaveResponse(this.organizerService.create(organizer));
    }
  }

  private createFromForm(): IOrganizer {
    return {
      ...new Organizer(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      url: this.editForm.get(['url'])!.value,
      email: this.editForm.get(['email'])!.value,
      bankAccountNumber: this.editForm.get(['bankAccountNumber'])!.value,
      bankAccountName: this.editForm.get(['bankAccountName'])!.value,
      bankName: this.editForm.get(['bankName'])!.value,
      mdr: this.editForm.get(['mdr'])!.value,
      sharing: this.editForm.get(['sharing'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      lastUpdatedAt: this.editForm.get(['lastUpdatedAt'])!.value
        ? moment(this.editForm.get(['lastUpdatedAt'])!.value, DATE_TIME_FORMAT)
        : undefined,
      status: this.editForm.get(['status'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrganizer>>): void {
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
}
