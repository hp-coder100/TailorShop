import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TailorPageComponent } from './tailor-page.component';

describe('TailorPageComponent', () => {
  let component: TailorPageComponent;
  let fixture: ComponentFixture<TailorPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TailorPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TailorPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
