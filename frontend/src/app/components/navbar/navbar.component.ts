import { CommonModule } from '@angular/common';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/authservice.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
})
export class NavbarComponent implements OnInit {
  @ViewChild('toggleButton') toggle!: ElementRef;
  @ViewChild('dropDown') dropDown!: ElementRef<HTMLDivElement>;

  checkInterval: any;
  isLoggedIn: boolean = false;
  userType: string | null = null;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit() {
    this.checkInterval = setInterval(() => {
      // console.log('log');
      let tokenExpiration = window.sessionStorage.getItem('expiry');
      let token = window.sessionStorage.getItem('token');
      if (
        token != null ||
        (tokenExpiration != null && new Date(+tokenExpiration) > new Date())
      ) {
        this.isLoggedIn = true;
      } else {
        window.sessionStorage.clear();
        this.isLoggedIn = false;
      }

      this.userType = window.sessionStorage.getItem('role');
    }, 1000);
  }

  logout() {
    if (window.confirm('Are you sure ? you want to logout.')) {
      this.isLoggedIn = false;
      this.authService.logout();
      this.router.navigate(['']);
    }
  }

  closeDropDown() {
    setTimeout(() => {
      if (this.dropDown.nativeElement.classList.contains('show')) {
        this.toggle.nativeElement.click();
      }
    }, 5000);
  }

  ngOnDestroy() {
    this.checkInterval.clear();
  }
}
