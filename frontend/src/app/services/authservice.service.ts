import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private isLoggedIn: boolean = false;

  constructor() {
    this.isLoggedIn = !!window.sessionStorage.getItem('token');
  }
  isAuthinticated() {
    let tokenExpiration = window.sessionStorage.getItem('expiry');

    if (
      tokenExpiration != null &&
      new Date(+tokenExpiration) > new Date() &&
      this.isLoggedIn
    ) {
      return true;
    } else {
      window.sessionStorage.clear();
      this.isLoggedIn = false;
      return false;
    }
  }

  login(token: string, role: string, userId: number) {
    window.sessionStorage.setItem('token', token);
    window.sessionStorage.setItem('role', role);
    window.sessionStorage.setItem('userId', '' + userId);
    window.sessionStorage.setItem(
      'expiry',
      '' + (new Date().getTime() + 24 * 60 * 60 * 1000 - 60000)
    );
    this.isLoggedIn = true;
    return true;
  }

  getUserDetails() {
    let role = window.sessionStorage.getItem('role');
    let userId = window.sessionStorage.getItem('userId') || -1;
    return {
      role: role,
      userId: +userId,
    };
  }

  logout(): void {
    window.sessionStorage.clear();
    this.isLoggedIn = false;
  }
}
