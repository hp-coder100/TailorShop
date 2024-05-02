import { CommonModule, Location } from '@angular/common';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Appointment } from '../../models/appointment.model';
import { Category } from '../../models/category.model';
import { Customer } from '../../models/customer.model';
import { Measurement } from '../../models/measurement.model';
import { Payment } from '../../models/payment.model';
import { HttpService } from '../../services/httpService/http.service';

@Component({
  selector: 'app-appointment-details',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './appointment-details.component.html',
  styleUrl: './appointment-details.component.css',
})
export class AppointmentDetailsComponent {
  @ViewChild('buttonModelShow') buttonModel!: ElementRef;
  currentDate: string = new Date(new Date().setDate(new Date().getDate() + 1))
    .toISOString()
    .substring(0, 10);
  paymentAmount: number = 0;
  paymentRecieved: boolean = false;
  measurementDetails: string = '';
  updatedDate!: Date;
  appointmentId!: number;
  modelTitle!: string;
  appointment: Appointment = new Appointment(0, new Date(), '', 0, 0, 0);
  customer: Customer = new Customer(0, '', '', '', 0, '', '');
  category: Category = new Category(0, '', '', 0);

  paymentDetails: Payment[] = [];
  measurementDetaisl: Measurement[] = [];
  showDetails: string = 'Payment';
  constructor(
    private httpService: HttpService,
    private route: ActivatedRoute,
    private location: Location
  ) {}
  ngOnInit() {
    this.appointmentId = this.route.snapshot.params['id'];
    this.loadAppointment(this.appointmentId);
  }

  loadPaymentDetails() {
    this.httpService.findAllPayment(this.appointmentId).subscribe({
      next: (data: any) => (this.paymentDetails = data),
      error: (error: any) => {
        console.log(error);
        if (!error?.error) {
          alert('Something went wrong');
        }
      },
    });
  }

  loadMeasurementDetails() {
    this.httpService
      .findAllMeasurements(this.appointment.shopId, this.appointmentId)
      .subscribe({
        next: (data: any) => (this.measurementDetaisl = data),
        error: (error: any) => {
          console.log(error);
          if (!error?.error) {
            alert('Something went wrong');
          }
        },
      });
  }

  loadCustomerDetails() {
    this.httpService.findCustomerById(this.appointment.customerId).subscribe({
      next: (data: any) => (this.customer = data),
      error: (error: any) => {
        console.log(error);
        if (!error?.error) {
          alert('Something went wrong');
        }
      },
    });
  }
  loadCategoryDetails() {
    this.httpService.findCategoryById(this.appointment.categoryId).subscribe({
      next: (data: any) => (this.category = data),
      error: (error: any) => {
        console.log(error);
        if (!error?.error) {
          alert('Something went wrong');
        }
      },
    });
  }

  loadAppointment(id: number) {
    this.httpService.findAppointmentById(id).subscribe({
      next: (data: any) => {
        this.appointment = data;
        this.loadMeasurementDetails();
        this.loadPaymentDetails();
        this.loadCustomerDetails();
        this.loadCategoryDetails();
      },
      error: (error: any) => {
        console.log(error);
        if (!error?.error) {
          alert('Something went wrong');
        }
      },
    });
  }

  updateAppointment(status: string) {
    this.httpService
      .updateAppointment({ ...this.appointment, status: status })
      .subscribe({
        next: (data: any) => {
          this.loadAppointment(this.appointmentId);
          window.alert('Appointment Status Changed to : ' + data.status);
        },
        error: (error: any) => {
          console.log(error);
          if (!error?.error) {
            alert('Something went wrong');
          }
        },
      });
  }
  updateAppointmentDate() {
    this.httpService
      .updateAppointment({
        ...this.appointment,
        appointmentDate: this.updatedDate,
      })
      .subscribe({
        next: (data: any) => {
          this.loadAppointment(this.appointmentId);
          this.buttonModel.nativeElement.click();
          window.alert('Appointment Date Changed to : ' + this.updatedDate);
          this.updatedDate = new Date();
        },
        error: (error: any) => {
          console.log(error);
          if (!error?.error) {
            alert('Something went wrong');
          }
        },
      });
  }
  saveMeasurementDetails() {
    let measurement = new Measurement(
      0,
      this.measurementDetails,
      this.appointmentId,
      this.appointment.customerId,
      this.appointment.shopId
    );
    this.httpService.createMeasurement(measurement).subscribe({
      next: (data: any) => {
        console.log(data as Measurement);
        this.buttonModel.nativeElement.click();
        this.loadMeasurementDetails();
        window.alert('Measurement Details Successfully Saved');
      },
      error: (error: any) => {
        console.log(error);
        if (!error?.error) {
          alert('Something went wrong');
        }
      },
    });
  }

  savePaymentDetails() {
    if (this.paymentAmount === 0) return;
    let payemnt = new Payment(
      0,
      this.paymentAmount,
      new Date(),
      this.paymentRecieved ? 'Done' : 'Pending',
      this.appointmentId
    );
    this.httpService.createPayment(payemnt).subscribe({
      next: (data: any) => {
        console.log(data as Payment);
        this.buttonModel.nativeElement.click();
        this.loadPaymentDetails();
        window.alert('Payment Details Successfully Saved');
      },
      error: (error: any) => {
        console.log(error);
        if (!error?.error) {
          alert('Something went wrong');
        }
      },
    });
  }
  changeAppointmentDate() {
    this.modelTitle = 'Change Date';
    this.buttonModel.nativeElement.click();
  }
  recordMeasurement() {
    this.modelTitle = 'Record Measurement';
    this.buttonModel.nativeElement.click();
  }
  recordPayment() {
    this.modelTitle = 'Record Payment';
    this.buttonModel.nativeElement.click();
  }

  goBack() {
    this.location.back();
  }
  loadPaymentTab() {
    this.loadPaymentDetails();
    console.log(this.paymentDetails, 'dsf');
    this.showDetails = 'Payment';
  }
  loadMeasurementTab() {
    this.loadMeasurementDetails();
    console.log(this.measurementDetaisl, 'dsf');
    this.showDetails = 'Measurement';
  }

  ngOnDestroy() {
    this.paymentDetails = [];
    this.measurementDetaisl = [];
  }
}
