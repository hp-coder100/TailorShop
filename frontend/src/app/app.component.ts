import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

import { FormsModule } from '@angular/forms';
import { RouterOutlet } from '@angular/router';
import { AppointmentsComponent } from './components/appointment/appointmentscomponent';
import { AuthComponent } from './components/auth/auth.component';
import { BookAppointmentComponent } from './components/book-appointment/book-appointment.component';

import { LandingPageComponent } from './components/landing-page/landing-page.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { TailorPageComponent } from './components/tailor-page/tailor-page.component';
import { AuthService } from './services/authservice.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    NavbarComponent,
    AppointmentsComponent,
    AuthComponent,
    TailorPageComponent,
    LandingPageComponent,
    BookAppointmentComponent,
    CommonModule,
    FormsModule,
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'tailorshop';

  isLoggedIn: boolean = false;

  constructor(private authService: AuthService) {}

  ngOnInit() {
    this.isLoggedIn = this.authService.isAuthinticated();
  }
}
