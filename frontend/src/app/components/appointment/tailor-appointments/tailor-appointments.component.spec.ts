import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TailorAppointmentsComponent } from './tailor-appointments.component';

describe('TailorAppointmentsComponent', () => {
  let component: TailorAppointmentsComponent;
  let fixture: ComponentFixture<TailorAppointmentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TailorAppointmentsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TailorAppointmentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
