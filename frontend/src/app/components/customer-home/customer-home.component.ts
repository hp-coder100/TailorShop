import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Tailor } from '../../models/tailor.model';
import { HttpService } from '../../services/httpService/http.service';

@Component({
  selector: 'app-customer-home',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './customer-home.component.html',
  styleUrl: './customer-home.component.css',
})
export class CustomerHomeComponent {
  tailors: Tailor[] = [];
  constructor(private httpService: HttpService) {}

  ngOnInit() {
    this.httpService.findAllTailors().subscribe({
      next: (data: any) => {
        this.tailors = data as Tailor[];
      },
      error: (error: any) => console.log(error),
    });
  }
}
