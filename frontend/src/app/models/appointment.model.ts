export class Appointment {
  public appointmentId: number;
  public appointmentDate: Date;
  public status: string;
  public customerId: number;
  public shopId: number;
  public categoryId: number;

  constructor(
    appointmentId: number,
    appointmentDate: Date,
    status: string,
    customerId: number,
    shopId: number,
    categoryId: number
  ) {
    this.appointmentId = appointmentId || 0;
    this.appointmentDate = appointmentDate || null;
    this.status = status || 'Pending';
    this.customerId = customerId || 0;
    this.shopId = shopId || 0;
    this.categoryId = categoryId || 0;
  }
}
