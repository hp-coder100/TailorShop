import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Output, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Tailor } from '../../../models/tailor.model';
import { HttpService } from '../../../services/httpService/http.service';

@Component({
  selector: 'app-tailor-signup',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './tailor-signup.component.html',
  styleUrl: './tailor-signup.component.css',
})
export class TailorSignupComponent {
  @ViewChild('signUpForm')
  singUpForm!: NgForm;
  @Output('modelTitle')
  modelTitle: EventEmitter<string> = new EventEmitter();
  @Output('closeModel') closeModel: EventEmitter<void> =
    new EventEmitter<void>();

  tailorData: Tailor = new Tailor(0, '', '', '', '');
  constructor(private httpService: HttpService) {}
  formError: string = '';
  onSignUp() {
    console.log(this.tailorData);
    this.httpService.singUpTailor(this.tailorData).subscribe({
      next: () => {
        this.singUpForm.form.reset();
        this.closeModel.emit();
        window.alert('Tailor Successcully Registered. You can Login now');
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
