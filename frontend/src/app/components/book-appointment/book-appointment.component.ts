import { CommonModule, Location } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Appointment } from '../../models/appointment.model';
import { Category } from '../../models/category.model';
import { HttpService } from '../../services/httpService/http.service';

@Component({
  selector: 'app-book-appointment',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './book-appointment.component.html',
  styleUrl: './book-appointment.component.css',
})
export class BookAppointmentComponent {
  currentDate: string = new Date(new Date().setDate(new Date().getDate() + 1))
    .toISOString()
    .substring(0, 10);

  bookedSuccessfully: boolean = false;
  message: string = '';
  appointmentDate!: Date;
  userId!: string;
  shopId!: number;
  categoryData: Category = new Category(0, '', '', 0);
  appointment!: Appointment;
  constructor(
    private httpService: HttpService,
    private route: ActivatedRoute,
    private location: Location
  ) {}

  ngOnInit() {
    this.categoryData.categoryId = this.route.snapshot.params['id'];
    this.shopId = this.route.snapshot.params['shopId'];
    this.userId = window.sessionStorage.getItem('userId') || '';
    console.log(this.userId);
    this.httpService.findCategoryById(this.categoryData.categoryId).subscribe({
      next: (data: any) => (this.categoryData = data),
      error: (error: any) => console.log(error),
    });
  }

  bookAppointment() {
    this.appointment = new Appointment(
      0,
      this.appointmentDate,
      'Pending',
      +this.userId,
      this.shopId,
      this.categoryData.categoryId
    );
    console.log(this.appointment);
    this.httpService.createAppointment(this.appointment).subscribe({
      next: (data: any) => {
        this.message =
          'You have successfully booked your appointment on date ' +
          data.appointmentDate +
          '. You will get updates regarding this appointment on your phone number.';
        this.bookedSuccessfully = true;
      },
      error: () => {
        this.message = 'Something went wrong. Please try after sometime.';
        this.bookedSuccessfully = false;
      },
    });
  }
  goBack() {
    this.location.back();
  }
}
