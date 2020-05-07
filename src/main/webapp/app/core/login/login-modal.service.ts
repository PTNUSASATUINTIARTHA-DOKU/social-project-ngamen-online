import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

import { LoginModalComponent } from 'app/shared/login/login.component';

@Injectable({ providedIn: 'root' })
export class LoginModalService {
  private isOpen = false;

  constructor(private modalService: NgbModal) {}

  open(): void {
    if (this.isOpen) {
      return;
    }
    this.isOpen = true;
    const modalRef: NgbModalRef = this.modalService.open(LoginModalComponent, {
      windowClass: 'custom-modal',
      backdropClass: 'fixed top-0 left-0 w-screen h-screen bg-gray-900 opacity-50'
    });
    modalRef.result.finally(() => (this.isOpen = false));
  }
}
