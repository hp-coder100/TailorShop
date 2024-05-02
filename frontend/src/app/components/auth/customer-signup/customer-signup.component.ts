import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Output, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Customer } from '../../../models/customer.model';
import { HttpService } from '../../../services/httpService/http.service';

@Component({
  selector: 'app-customer-signup',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './customer-signup.component.html',
  styleUrl: './customer-signup.component.css',
})
export class CustomerSignupComponent {
  @ViewChild('signUpForm')
  singUpForm!: NgForm;

  @Output('modelTitle')
  modelTitle: EventEmitter<string> = new EventEmitter();
  @Output('closeModel') closeModel: EventEmitter<void> =
    new EventEmitter<void>();

  customerData: Customer = new Customer(0, '', '', '', 0, '', '');

  constructor(private httpService: HttpService) {}
  formError: string = '';
  onSignUp() {
    this.httpService.singUpCustomer(this.customerData).subscribe({
      next: () => {
        this.singUpForm.form.reset();
        this.closeModel.emit();
        window.alert('Customer Registered Successfully. You can login now');
      },
      error: (error: any) => {
        console.log(error);
        this.formError = error?.error?.message;
        if (!this.formError) alert('Something went wrong');
      },
    });
  }
  onTitleChange(value: string) {
    this.modelTitle.emit(value);
  }
}
