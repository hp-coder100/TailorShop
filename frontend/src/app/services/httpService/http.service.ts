import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AppConstants } from '../../constants/app.constants';
import { environment } from '../../../environments/environment';
import { User } from '../../models/login.model';
import { Tailor } from '../../models/tailor.model';
import { Customer } from '../../models/customer.model';
import { Appointment } from '../../models/appointment.model';
import { Measurement } from '../../models/measurement.model';
import { Payment } from '../../models/payment.model';
import { Category } from '../../models/category.model';

@Injectable({
  providedIn: 'root',
})
export class HttpService {
  httpHeader!: HttpHeaders;
  constructor(private http: HttpClient) {}

  /* Auth Services */

  loginUser(user: User) {
    return this.http.post(
      environment.rooturl + AppConstants.LOGIN_API_URL,
      user
    );
  }

  singUpTailor(tailor: Tailor) {
    return this.http.post(
      environment.rooturl + AppConstants.TAILOR_SIGNUP_URL,
      tailor
    );
  }
  singUpCustomer(customer: Customer) {
    return this.http.post(
      environment.rooturl + AppConstants.CUSTOMER_SIGNUP_URL,
      customer
    );
  }

  //customer

  findCustomerById(id: Number) {
    return this.http.get(
      environment.rooturl + AppConstants.FIND_CUSTOMER_BY_ID + id,
      {
        withCredentials: true,
      }
    );
  }
  updateCustomer(customer: Customer) {
    return this.http.put(
      environment.rooturl + AppConstants.UPDATE_CUSTOMER,
      customer,
      { withCredentials: true }
    );
  }

  /* Tailor Service  */
  findAllTailors() {
    return this.http.get(environment.rooturl + AppConstants.FIND_ALL_TAILORS, {
      withCredentials: true,
    });
  }

  findTailorById(id: number) {
    return this.http.get(
      environment.rooturl + AppConstants.FIND_TAILOR_BY_ID + id,
      {
        withCredentials: true,
      }
    );
  }

  updateTailor(tailor: Tailor) {
    return this.http.put(
      environment.rooturl + AppConstants.UPDATE_TAILOR,
      tailor,
      { withCredentials: true }
    );
  }
  /* Category Service */
  createCategory(category: Category) {
    return this.http.post(
      environment.rooturl + AppConstants.CREATE_CATEGORY,
      category,
      { withCredentials: true }
    );
  }
  udpateCategory(category: Category) {
    return this.http.put(
      environment.rooturl + AppConstants.UPDATE_CATEGORY,
      category,
      { withCredentials: true }
    );
  }

  findCategoryById(id: number) {
    return this.http.get(
      environment.rooturl + AppConstants.FIND_CATEGORY_BY_ID + id,
      { withCredentials: true }
    );
  }
  deleteCategoryById(id: number) {
    return this.http.delete(
      environment.rooturl + AppConstants.DELETE_CATEGORY_BY_ID + id,
      { withCredentials: true }
    );
  }

  findCategoriesByShopId(id: number) {
    return this.http.get(
      environment.rooturl + AppConstants.FIND_CATEGORIES_BY_SHOP_ID + id,
      {
        withCredentials: true,
      }
    );
  }

  /* Appointment Services */

  createAppointment(appointment: Appointment) {
    return this.http.post(
      environment.rooturl + AppConstants.CREATE_APPOINTMENT,
      appointment,
      {
        withCredentials: true,
      }
    );
  }

  updateAppointment(appointment: Appointment) {
    return this.http.put(
      environment.rooturl + AppConstants.UPDATE_APPOINTMENT,
      appointment,
      {
        withCredentials: true,
      }
    );
  }

  findAppointmentByCustomerId(id: Number) {
    return this.http.get(
      environment.rooturl + AppConstants.FIND_APPOINTMENT_BY_CUSTOMER_ID + id,
      {
        withCredentials: true,
      }
    );
  }
  findAppointmentByShopId(id: Number) {
    return this.http.get(
      environment.rooturl + AppConstants.FIND_APPOINTMENT_BY_SHOP_ID + id,
      {
        withCredentials: true,
      }
    );
  }
  findAppointmentById(id: Number) {
    return this.http.get(
      environment.rooturl + AppConstants.FIND_APPOINTMENT_BY_ID + id,
      {
        withCredentials: true,
      }
    );
  }

  //Measurement Service
  createMeasurement(measurement: Measurement) {
    return this.http.post(
      environment.rooturl + AppConstants.CREATE_MEASUREMENT,
      measurement,
      { withCredentials: true }
    );
  }

  //Payment Services
  createPayment(payemnt: Payment) {
    console.log(payemnt);
    return this.http.post(
      environment.rooturl + AppConstants.CREATE_PAYMENT,
      payemnt,
      { withCredentials: true }
    );
  }

  findAllPayment(appointmentId: number) {
    return this.http.get(
      environment.rooturl +
        AppConstants.FIND_ALL_PAYEMNT +
        '?appointmentId=' +
        appointmentId,
      { withCredentials: true }
    );
  }
  findAllMeasurements(shopId: number, appointmentId: number) {
    return this.http.get(
      environment.rooturl +
        AppConstants.FIND_ALL_MEASUREMENTS +
        shopId +
        '?appointmentId=' +
        appointmentId,
      { withCredentials: true }
    );
  }
}
