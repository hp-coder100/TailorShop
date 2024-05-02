import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Appointment } from '../../../models/appointment.model';
import { HttpService } from '../../../services/httpService/http.service';

@Component({
  selector: 'app-tailor-appointments',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './tailor-appointments.component.html',
  styleUrl: './tailor-appointments.component.css',
})
export class TailorAppointmentsComponent {
  selectedAppointment!: Appointment;
  searchAppointmentId!: number;
  appointments: Appointment[] = [];
  constructor(private httpService: HttpService, private router: Router) {}

  ngOnInit() {
    this.loadAppointments();
  }

  loadAppointments() {
    let id = window.sessionStorage.getItem('userId') || 0;
    this.httpService.findAppointmentByShopId(+id).subscribe({
      next: (data: any) => {
        this.appointments = data;
        this.appointments.reverse();
      },
      error: (error: any) => console.log(error),
    });
  }
  searchAppointment() {
    if (this.searchAppointmentId === 0 || this.searchAppointment === undefined)
      return false;
    else {
      console.log(this.searchAppointmentId);
      let res = this.appointments.find(
        (ap) => ap.appointmentId === (this.searchAppointmentId as number)
      );
      console.log(res);
      if (res) {
        this.router.navigate([
          '/appointments/viewDetails',
          this.searchAppointmentId,
        ]);
        return true;
      } else {
        alert(
          "Appointment doesn't exits with appointment Id : " +
            this.searchAppointmentId
        );
        return false;
      }
    }
  }
}
