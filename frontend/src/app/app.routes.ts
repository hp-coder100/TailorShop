import { Routes } from '@angular/router';
import { AppointmentDetailsComponent } from './components/appointment-details/appointment-details.component';
import { AppointmentsComponent } from './components/appointment/appointmentscomponent';
import { BookAppointmentComponent } from './components/book-appointment/book-appointment.component';
import { CustomerHomeComponent } from './components/customer-home/customer-home.component';
import { CustomerProfileComponent } from './components/customer-profile/customer-profile.component';
import { FeaturesComponent } from './components/features/features.component';
import { ForbiddenPageComponent } from './components/forbidden-page/forbidden-page.component';

import { LandingPageComponent } from './components/landing-page/landing-page.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { TailorPageComponent } from './components/tailor-page/tailor-page.component';
import { AuthGuard } from './guards/authgaurd.guard';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    component: LandingPageComponent,
  },
  {
    path: 'customerHome',
    component: CustomerHomeComponent,
    canActivate: [AuthGuard],
    data: {
      roles: ['ROLE_USER'],
    },
  },

  {
    path: 'customerProfile',
    component: CustomerProfileComponent,
    canActivate: [AuthGuard],
    data: {
      roles: ['ROLE_USER'],
    },
  },

  {
    path: 'tailorHome',
    component: TailorPageComponent,
    canActivate: [AuthGuard],
    data: {
      roles: ['ROLE_TAILOR'],
    },
  },
  {
    path: 'appointments',
    component: AppointmentsComponent,
    canActivate: [AuthGuard],
    data: {
      roles: ['ROLE_USER', 'ROLE_TAILOR'],
    },
  },
  {
    path: 'appointments/viewDetails/:id',
    component: AppointmentDetailsComponent,
    canActivate: [AuthGuard],
    data: {
      roles: ['ROLE_TAILOR'],
    },
  },
  {
    path: 'category/bookAppointment/tailor/:shopId/category/:id',
    component: BookAppointmentComponent,
    canActivate: [AuthGuard],
    data: {
      roles: ['ROLE_USER'],
    },
  },

  {
    path: 'tailor/:id',
    component: TailorPageComponent,
    canActivate: [AuthGuard],
    data: {
      roles: ['ROLE_USER', 'ROLE_TAILOR'],
    },
  },
  {
    path: 'features',
    component: FeaturesComponent,
  },
  {
    path: 'forbidden',
    component: ForbiddenPageComponent,
  },
  {
    path: '**',
    component: PageNotFoundComponent,
  },
];
