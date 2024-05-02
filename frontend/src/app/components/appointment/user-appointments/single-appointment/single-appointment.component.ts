import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Appointment } from '../../../../models/appointment.model';
import { Category } from '../../../../models/category.model';
import { Tailor } from '../../../../models/tailor.model';
import { HttpService } from '../../../../services/httpService/http.service';

@Component({
  selector: 'app-single-appointment',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './single-appointment.component.html',
  styleUrl: './single-appointment.component.css',
})
export class SingleAppointmentComponent {
  @Input({ required: true }) selectedAppointment!: Appointment;

  tailor!: Tailor;

  category: Category = new Category(0, '', '', 0);

  constructor(private httpService: HttpService) {}

  ngOnInit() {
    if (this.selectedAppointment) {
      this.httpService
        .findTailorById(this.selectedAppointment.shopId)
        .subscribe({
          next: (data: any) => (this.tailor = data),
          error: (error: any) => console.log(error),
        });

      this.httpService
        .findCategoryById(this.selectedAppointment.categoryId)
        .subscribe({
          next: (data: any) => {
            this.category = data;
          },
          error: (error: any) => console.log(error),
        });
    }
  }
}
