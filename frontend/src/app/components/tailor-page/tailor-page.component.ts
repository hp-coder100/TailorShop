import { CommonModule } from '@angular/common';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Category } from '../../models/category.model';
import { Tailor } from '../../models/tailor.model';
import { HttpService } from '../../services/httpService/http.service';

@Component({
  selector: 'app-tailor-page',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './tailor-page.component.html',
  styleUrl: './tailor-page.component.css',
})
export class TailorPageComponent implements OnInit {
  @ViewChild('closeButton') closeBtn!: ElementRef;
  @ViewChild('openModel') openBtn!: ElementRef;
  categoryToSubmit: Category = new Category(0, '', '', 0);

  modelTitle!: string;
  formError: string = '';
  asTailor: boolean = false;
  tailor: Tailor = new Tailor(0, '', '', '', '');
  showCategory: boolean = false;
  categories: Category[] = [
    new Category(0, 'sdfjkllasdf', 'lsjfjdlasjldfjls', 1),
  ];

  constructor(
    private httpService: HttpService,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    let tailorId = this.route.snapshot.params['id'];

    if (!tailorId) {
      if (window.sessionStorage.getItem('role') === 'ROLE_TAILOR') {
        tailorId = window.sessionStorage.getItem('userId');
        this.asTailor = true;
        this.loadTailor(tailorId);
        this.loadCategory(tailorId);
      } else {
        alert('Something went wrong!');
      }
    } else {
      this.loadTailor(tailorId);
      this.loadCategory(tailorId);
    }
  }

  loadCategory(tailorId: number) {
    this.httpService.findCategoriesByShopId(tailorId).subscribe({
      next: (data: any) => (this.categories = data as Category[]),
      error: (error: any) => console.log(error),
    });
  }

  loadTailor(tailorId: number) {
    this.httpService.findTailorById(tailorId).subscribe({
      next: (data: any) => (this.tailor = data),
      error: (error: any) => console.log(error),
    });
  }

  updateCategory() {
    this.httpService.udpateCategory(this.categoryToSubmit).subscribe({
      next: (data: any) => {
        this.loadCategory(this.tailor.shopId);
        window.alert('Category Update Successfully');
        this.closeBtn.nativeElement.click();
      },
      error: (error: any) => {
        console.log(error);
        this.formError = 'Something went Wrong';
      },
    });
  }

  addCategory() {
    this.categoryToSubmit = {
      ...this.categoryToSubmit,
      shopId: this.tailor.shopId,
    };
    this.httpService.createCategory(this.categoryToSubmit).subscribe({
      next: (data: any) => {
        window.alert('Category Added Successfully');
        console.log(data);
        this.loadCategory(this.tailor.shopId);
        this.closeBtn.nativeElement.click();
      },
      error: (error: any) => {
        console.log(error);
        this.formError = 'Something went Wrong';
      },
    });
  }

  submitCategory() {
    if (this.modelTitle === 'Update Category') {
      this.updateCategory();
      return true;
    } else if (this.modelTitle === 'Add New Category') {
      this.addCategory();
      return true;
    }
    return false;
  }
  deleteCategory(id: number) {
    if (window.confirm('Are you sure ? you want to delete this category')) {
      this.httpService.deleteCategoryById(id).subscribe({
        next: (data: any) => {
          console.log(data);
          window.alert('Category Deleted Successfully.');
          this.loadCategory(this.tailor.shopId);
        },
        error: (error: any) => console.log(error),
      });
    }
  }
  updateCategoryModel(category: Category) {
    this.categoryToSubmit = category;
    this.modelTitle = 'Update Category';
    this.openBtn.nativeElement.click();
  }
  addCategoryModel() {
    this.categoryToSubmit = new Category(0, '', '', this.tailor.shopId);
    this.modelTitle = 'Add New Category';
    this.openBtn.nativeElement.click();
  }

  editDescriptionModal() {
    this.modelTitle = 'Update Description';
    this.openBtn.nativeElement.click();
  }

  updateDescription() {
    this.httpService.updateTailor(this.tailor).subscribe({
      next: (data: any) => {
        console.log(data);
        window.alert('Description Update successfully.');
        this.loadTailor(this.tailor.shopId);
        this.closeBtn.nativeElement.click();
      },
      error: (error: any) => {
        console.log(error);
        window.alert('Something went wrong');
        this.closeBtn.nativeElement.click();
      },
    });
  }
}
