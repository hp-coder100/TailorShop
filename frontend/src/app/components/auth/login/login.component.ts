import { CommonModule } from '@angular/common';
import {
  Component,
  EventEmitter,
  Input,
  Output,
  ViewChild,
} from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from '../../../models/login.model';
import { AuthService } from '../../../services/authservice.service';
import { HttpService } from '../../../services/httpService/http.service';
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  @ViewChild('loginForm')
  loginForm!: NgForm;

  @Output('modelTitle') modelTitle: EventEmitter<string> = new EventEmitter();

  @Output('closeModel') closeModel: EventEmitter<void> =
    new EventEmitter<void>();
  loginType: string = 'customer';

  formError: string = '';

  userData: User = { username: '', password: '' };

  constructor(
    private httpService: HttpService,
    private authService: AuthService,
    private route: Router
  ) {}
  onLogin() {
    this.httpService.loginUser(this.userData).subscribe({
      next: (data: any) => {
        // window.sessionStorage.setItem('token', data.token);
        if (this.authService.login(data.token, data.role, data.userId)) {
          this.loginForm.form.reset();
          this.closeModel.emit();
          window.alert('Login Success');

          if (data.role == 'ROLE_USER') this.route.navigate(['/customerHome']);
          else this.route.navigate(['/tailorHome']);
        }
      },
      error: (err: any) => {
        this.formError = err?.error?.message;
        if (!this.formError) alert('Something went wrong');
        console.log(err);
      },
    });
  }

  onTitleChange() {
    this.modelTitle.emit('Customer | SignUp');
  }
}
