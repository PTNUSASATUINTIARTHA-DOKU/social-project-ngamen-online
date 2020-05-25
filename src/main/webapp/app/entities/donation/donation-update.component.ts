import { HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit, HostListener } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { OrganizerService } from 'app/entities/organizer/organizer.service';
import { PATTERN_URL } from 'app/shared/constants/pattern.constants';
import { Donation, IDonation } from 'app/shared/model/donation.model';
import { IOrganizer } from 'app/shared/model/organizer.model';
import { StorageService } from 'app/shared/service/storage.service';
import { Observable, Subscription } from 'rxjs';
import { DonationService } from './donation.service';

@Component({
  selector: 'jhi-donation-update',
  templateUrl: './donation-update.component.html'
})
export class DonationUpdateComponent implements OnInit, OnDestroy {
  isSaving = false;
  organizers: IOrganizer[] = [];
  generatedFileName = '';
  subscription: Subscription = new Subscription();
  donation: Donation = {};
  windowScrolled: boolean | undefined;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(100)]],
    description: [null, [Validators.maxLength(2000)]],
    url: [null, [Validators.maxLength(100), Validators.pattern(PATTERN_URL)]],
    logo: [null, [Validators.maxLength(200)]],
    logoStyle: [null, [Validators.maxLength(500)]],
    paymentSlug: [null, [Validators.maxLength(100)]],
    bankAccountNumber: [null, [Validators.maxLength(15)]],
    bankAccountName: [null, [Validators.maxLength(100)]],
    bankName: [null, [Validators.maxLength(100)]],
    chainMallId: [],
    status: [],
    organizer: []
  });

  constructor(
    protected donationService: DonationService,
    protected organizerService: OrganizerService,
    protected storageService: StorageService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  @HostListener('window:scroll', [])
  onWindowScroll(): void {
    if (window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop > 20) {
      this.windowScrolled = true;
    } else if ((this.windowScrolled && window.pageYOffset) || document.documentElement.scrollTop || document.body.scrollTop < 10) {
      this.windowScrolled = false;
    }
  }
  scrollToTop(): void {
    const currentScroll = document.documentElement.scrollTop || document.body.scrollTop;
    if (currentScroll > 0) {
      window.scrollTo(0, currentScroll - currentScroll / 0);
    }
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ donation }) => {
      this.donation = donation;
      this.updateForm(donation);
      if (!this.editForm.get('url')?.value) this.editForm.patchValue({ url: 'http' });
      this.organizerService.query().subscribe((res: HttpResponse<IOrganizer[]>) => (this.organizers = res.body || []));
      this.editForm.get('paymentSlug')?.disable();
      this.editForm.get('logo')?.disable();
    });
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  updateForm(donation: IDonation): void {
    this.editForm.patchValue({
      id: donation.id,
      name: donation.name,
      description: donation.description,
      url: donation.url,
      logo: donation.logo,
      logoStyle: donation.logoStyle,
      paymentSlug: donation.paymentSlug,
      bankAccountNumber: donation.bankAccountNumber,
      bankAccountName: donation.bankAccountName,
      bankName: donation.bankName,
      chainMallId: donation.chainMallId,
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
      logo: this.editForm.get(['logo'])!.value,
      logoStyle: this.editForm.get(['logoStyle'])!.value,
      paymentSlug: this.editForm.get(['paymentSlug'])!.value,
      bankAccountNumber: this.editForm.get(['bankAccountNumber'])!.value,
      bankAccountName: this.editForm.get(['bankAccountName'])!.value,
      bankName: this.editForm.get(['bankName'])!.value,
      chainMallId: this.editForm.get(['chainMallId'])!.value,
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

  generateSlug(): void {
    this.editForm.patchValue({
      paymentSlug: this.editForm
        .get(['name'])!
        .value.replace(/\W+/g, '-')
        .replace(/-$/, '')
        .toLowerCase()
    });
  }

  onFileSelected(event: Event): void {
    const target = event.target as HTMLInputElement;
    if (target.files) {
      const file = target.files[0];
      this.storageService.upload(file);
      this.subscription = this.storageService.sharedFilename.subscribe(filename => {
        this.editForm.patchValue({ logo: filename.valueOf() });
      });
    }
  }
}
