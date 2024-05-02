import { CommonModule } from '@angular/common';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../services/authservice.service';
import { AuthComponent } from '../auth/auth.component';

@Component({
  selector: 'app-landing-page',
  standalone: true,
  imports: [AuthComponent, RouterLink, CommonModule],
  templateUrl: './landing-page.component.html',
  styleUrl: './landing-page.component.css',
})
export class LandingPageComponent {
  isLoggedIn: boolean = false;
  interval: any = null;
  constructor(private authService: AuthService) {}

  ngOnInit() {
    this.interval = setInterval(() => {
      if (this.authService.isAuthinticated()) {
        this.isLoggedIn = true;
      } else {
        this.isLoggedIn = false;
      }
      // console.log(this.isLoggedIn);
    }, 1000);
  }
}
