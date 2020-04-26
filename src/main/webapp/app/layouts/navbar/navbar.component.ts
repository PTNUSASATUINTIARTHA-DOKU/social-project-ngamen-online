import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { SessionStorageService } from 'ngx-webstorage';

import { VERSION } from 'app/app.constants';
import { LANGUAGES } from 'app/core/language/language.constants';
import { AccountService } from 'app/core/auth/account.service';
import { LoginModalService } from 'app/core/login/login-modal.service';
import { LoginService } from 'app/core/login/login.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { Location } from '@angular/common';

@Component({
  selector: 'jhi-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['navbar.scss']
})
export class NavbarComponent implements OnInit {
  inProduction?: boolean;
  isNavbarCollapsed = false;
  isDropdown1Collapsed = false;
  isDropdown2Collapsed = false;
  isDropdown3Collapsed = false;
  isDropdown4Collapsed = false;
  languages = LANGUAGES;
  swaggerEnabled?: boolean;
  version: string;
  path: string;

  constructor(
    private loginService: LoginService,
    private languageService: JhiLanguageService,
    private sessionStorage: SessionStorageService,
    private accountService: AccountService,
    private loginModalService: LoginModalService,
    private profileService: ProfileService,
    private router: Router,
    private location: Location
  ) {
    this.version = VERSION ? (VERSION.toLowerCase().startsWith('v') ? VERSION : 'v' + VERSION) : '';
    this.path = location.path();
  }

  ngOnInit(): void {
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.swaggerEnabled = profileInfo.swaggerEnabled;
    });
  }

  changeLanguage(languageKey: string): void {
    this.sessionStorage.store('locale', languageKey);
    this.languageService.changeLanguage(languageKey);
  }

  collapseNavbar(): void {
    this.isNavbarCollapsed = true;
  }

  collapseDropdown1(): void {
    this.isDropdown1Collapsed = true;
  }

  collapseDropdown2(): void {
    this.isDropdown1Collapsed = true;
  }

  collapseDropdown3(): void {
    this.isDropdown3Collapsed = true;
  }

  collapseDropdown4(): void {
    this.isDropdown4Collapsed = true;
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    this.loginModalService.open();
  }

  logout(): void {
    this.collapseNavbar();
    this.loginService.logout();
    this.router.navigate(['']);
  }

  toggleNavbar(): void {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }

  toggleDropdown1(): void {
    this.isDropdown1Collapsed = !this.isDropdown1Collapsed;
  }

  toggleDropdown2(): void {
    this.isDropdown2Collapsed = !this.isDropdown2Collapsed;
  }

  toggleDropdown3(): void {
    this.isDropdown3Collapsed = !this.isDropdown3Collapsed;
  }

  toggleDropdown4(): void {
    this.isDropdown4Collapsed = !this.isDropdown4Collapsed;
  }

  getImageUrl(): string {
    return this.isAuthenticated() ? this.accountService.getImageUrl() : '';
  }
}
