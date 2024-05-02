import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Appointment } from '../../models/appointment.model';
import { HttpService } from '../../services/httpService/http.service';
import { TailorAppointmentsComponent } from './tailor-appointments/tailor-appointments.component';
import { SingleAppointmentComponent } from './user-appointments/single-appointment/single-appointment.component';
import { UserAppointmentsComponent } from './user-appointments/user-appointments.component';

@Component({
  selector: 'app-appointments',
  standalone: true,
  imports: [
    UserAppointmentsComponent,
    TailorAppointmentsComponent,
    CommonModule,
  ],
  templateUrl: './appointments.component.html',
  styleUrl: './appointments.component.css',
})
export class AppointmentsComponent implements OnInit {
  userType: string | null = null;

  ngOnInit(): void {
    this.userType = window.sessionStorage.getItem('role');
  }
}
