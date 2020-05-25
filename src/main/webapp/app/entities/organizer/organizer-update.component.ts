import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, HostListener } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { PATTERN_EMAIL, PATTERN_URL } from 'app/shared/constants/pattern.constants';
import { IOrganizer, Organizer } from 'app/shared/model/organizer.model';
import { Observable, Subscription } from 'rxjs';
import { OrganizerService } from './organizer.service';
import { StorageService } from 'app/shared/service/storage.service';

@Component({
  selector: 'jhi-organizer-update',
  templateUrl: './organizer-update.component.html'
})
export class OrganizerUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  generatedFileName = '';
  subscription: Subscription = new Subscription();
  windowScrolled: boolean | undefined;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(100)]],
    url: [null, [Validators.maxLength(100), Validators.pattern(PATTERN_URL)]],
    email: [null, [Validators.required, Validators.maxLength(100), Validators.pattern(PATTERN_EMAIL)]],
    logo: [null, [Validators.maxLength(200)]],
    logoStyle: [null, [Validators.maxLength(500)]],
    bankAccountNumber: [null, [Validators.maxLength(15)]],
    bankAccountName: [null, [Validators.maxLength(100)]],
    bankName: [null, [Validators.maxLength(100)]],
    mdr: [null, [Validators.max(100)]],
    sharing: [null, [Validators.max(100)]],
    mallId: [],
    sharedKey: [null, [Validators.maxLength(20)]],
    serviceId: [],
    acquirerId: [],
    status: [],
    users: []
  });

  constructor(
    protected organizerService: OrganizerService,
    protected userService: UserService,
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
    this.activatedRoute.data.subscribe(({ organizer }) => {
      this.updateForm(organizer);
      if (!this.editForm.get('url')?.value) this.editForm.patchValue({ url: 'http' });
      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
      this.editForm.get('logo')?.disable();
    });
  }

  updateForm(organizer: IOrganizer): void {
    this.editForm.patchValue({
      id: organizer.id,
      name: organizer.name,
      url: organizer.url,
      email: organizer.email,
      logo: organizer.logo,
      logoStyle: organizer.logoStyle,
      bankAccountNumber: organizer.bankAccountNumber,
      bankAccountName: organizer.bankAccountName,
      bankName: organizer.bankName,
      mdr: organizer.mdr,
      sharing: organizer.sharing,
      mallId: organizer.mallId,
      sharedKey: organizer.sharedKey,
      serviceId: organizer.serviceId,
      acquirerId: organizer.acquirerId,
      status: organizer.status,
      users: organizer.users
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
      logo: this.editForm.get(['logo'])!.value,
      logoStyle: this.editForm.get(['logoStyle'])!.value,
      bankAccountNumber: this.editForm.get(['bankAccountNumber'])!.value,
      bankAccountName: this.editForm.get(['bankAccountName'])!.value,
      bankName: this.editForm.get(['bankName'])!.value,
      mdr: this.editForm.get(['mdr'])!.value,
      sharing: this.editForm.get(['sharing'])!.value,
      mallId: this.editForm.get(['mallId'])!.value,
      sharedKey: this.editForm.get(['sharedKey'])!.value,
      serviceId: this.editForm.get(['serviceId'])!.value,
      acquirerId: this.editForm.get(['acquirerId'])!.value,
      status: this.editForm.get(['status'])!.value,
      users: this.editForm.get(['users'])!.value
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

  trackById(index: number, item: IUser): any {
    return item.id;
  }

  getSelected(selectedVals: IUser[], option: IUser): IUser {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
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
