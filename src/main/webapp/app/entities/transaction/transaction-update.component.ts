import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ITransaction, Transaction } from 'app/shared/model/transaction.model';
import { TransactionService } from './transaction.service';
import { IDonation } from 'app/shared/model/donation.model';
import { DonationService } from 'app/entities/donation/donation.service';

@Component({
  selector: 'jhi-transaction-update',
  templateUrl: './transaction-update.component.html'
})
export class TransactionUpdateComponent implements OnInit {
  isSaving = false;
  donations: IDonation[] = [];

  editForm = this.fb.group({
    id: [],
    invoiceNumber: [null, [Validators.required, Validators.maxLength(30)]],
    sessionId: [null, [Validators.maxLength(128)]],
    basket: [null, [Validators.maxLength(1024)]],
    ovoIdMasked: [null, [Validators.maxLength(15)]],
    deviceInformation: [null, [Validators.maxLength(1000)]],
    lastUpdatedBy: [null, [Validators.required, Validators.maxLength(100)]],
    lastUpdatedAt: [null, [Validators.required]],
    status: [null, [Validators.required]],
    donation: []
  });

  constructor(
    protected transactionService: TransactionService,
    protected donationService: DonationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transaction }) => {
      if (!transaction.id) {
        const today = moment().startOf('day');
        transaction.lastUpdatedAt = today;
      }

      this.updateForm(transaction);

      this.donationService.query().subscribe((res: HttpResponse<IDonation[]>) => (this.donations = res.body || []));
    });
  }

  updateForm(transaction: ITransaction): void {
    this.editForm.patchValue({
      id: transaction.id,
      invoiceNumber: transaction.invoiceNumber,
      sessionId: transaction.sessionId,
      basket: transaction.basket,
      ovoIdMasked: transaction.ovoIdMasked,
      deviceInformation: transaction.deviceInformation,
      lastUpdatedBy: transaction.lastUpdatedBy,
      lastUpdatedAt: transaction.lastUpdatedAt ? transaction.lastUpdatedAt.format(DATE_TIME_FORMAT) : null,
      status: transaction.status,
      donation: transaction.donation
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const transaction = this.createFromForm();
    if (transaction.id !== undefined) {
      this.subscribeToSaveResponse(this.transactionService.update(transaction));
    } else {
      this.subscribeToSaveResponse(this.transactionService.create(transaction));
    }
  }

  private createFromForm(): ITransaction {
    return {
      ...new Transaction(),
      id: this.editForm.get(['id'])!.value,
      invoiceNumber: this.editForm.get(['invoiceNumber'])!.value,
      sessionId: this.editForm.get(['sessionId'])!.value,
      basket: this.editForm.get(['basket'])!.value,
      ovoIdMasked: this.editForm.get(['ovoIdMasked'])!.value,
      deviceInformation: this.editForm.get(['deviceInformation'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      lastUpdatedAt: this.editForm.get(['lastUpdatedAt'])!.value
        ? moment(this.editForm.get(['lastUpdatedAt'])!.value, DATE_TIME_FORMAT)
        : undefined,
      status: this.editForm.get(['status'])!.value,
      donation: this.editForm.get(['donation'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransaction>>): void {
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

  trackById(index: number, item: IDonation): any {
    return item.id;
  }
}
