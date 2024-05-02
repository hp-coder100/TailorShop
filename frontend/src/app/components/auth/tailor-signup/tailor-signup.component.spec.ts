import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TailorSignupComponent } from './tailor-signup.component';

describe('TailorSignupComponent', () => {
  let component: TailorSignupComponent;
  let fixture: ComponentFixture<TailorSignupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TailorSignupComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TailorSignupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
