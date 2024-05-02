import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Appointment } from '../../../models/appointment.model';
import { HttpService } from '../../../services/httpService/http.service';
import { SingleAppointmentComponent } from './single-appointment/single-appointment.component';

@Component({
  selector: 'app-user-appointments',
  standalone: true,
  imports: [CommonModule, SingleAppointmentComponent],
  templateUrl: './user-appointments.component.html',
  styleUrl: './user-appointments.component.css',
})
export class UserAppointmentsComponent {
  selectedAppointment!: Appointment;

  appointments: Appointment[] = [];

  constructor(private httpService: HttpService) {}

  ngOnInit() {
    this.loadAppointments();
  }

  loadAppointments() {
    let id = window.sessionStorage.getItem('userId') || 0;
    this.httpService.findAppointmentByCustomerId(+id).subscribe({
      next: (data: any) => {
        this.appointments = data;
        this.appointments.reverse();
      },
      error: (error: any) => {
        console.log(error);
        if (!error?.error) {
          alert('Something went wrong');
        }
      },
    });
  }
}
