import { CommonModule } from '@angular/common';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CustomerSignupComponent } from './customer-signup/customer-signup.component';
import { LoginComponent } from './login/login.component';
import { TailorSignupComponent } from './tailor-signup/tailor-signup.component';

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [
    LoginComponent,
    TailorSignupComponent,
    CustomerSignupComponent,
    CommonModule,
  ],
  templateUrl: './auth.component.html',
  styleUrl: './auth.component.css',
})
export class AuthComponent {
  @ViewChild('closeButton') closeButton!: ElementRef;
  title: string = 'Login';

  onTitleChange(value: string) {
    this.title = value;
  }

  closeModal() {
    console.log(this.title);
    this.closeButton.nativeElement.click();
  }
}
