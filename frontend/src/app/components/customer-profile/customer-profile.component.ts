import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Customer } from '../../models/customer.model';
import { HttpService } from '../../services/httpService/http.service';

@Component({
  selector: 'app-customer-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './customer-profile.component.html',
  styleUrl: './customer-profile.component.css',
})
export class CustomerProfileComponent {
  formError: string = '';

  customerId!: number;
  editDetails: boolean = false;
  customerData: Customer = new Customer(0, '', '', '', 0, '', '');

  constructor(private httpService: HttpService) {}
  ngOnInit() {
    let id = window.sessionStorage.getItem('userId');
    let userType = window.sessionStorage.getItem('role');

    if (id === undefined || userType === 'ROLE_TAILOR') {
      alert('Something went wrong');
    } else {
      this.customerId = id ? +id : 0;
      this.loadCustomerDetails();
    }
  }

  loadCustomerDetails() {
    this.httpService.findCustomerById(this.customerId).subscribe({
      next: (data: any) => (this.customerData = data),
      error: (error: any) => console.log(error),
    });
  }
  onUpdate() {
    this.httpService.updateCustomer(this.customerData).subscribe({
      next: (data: any) => {
        this.loadCustomerDetails();
        this.editDetails = false;
        alert('Customer Updated Successfully.');
      },
      error: (error: any) => {
        console.log(error);
        this.formError = error?.error?.message;
      },
    });
  }
}
