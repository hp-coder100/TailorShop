export class Customer {
  public customerId: number;
  public customerFirstName: string;
  public customerLastName: string;
  public address: string;
  public phone: number;
  public email: string;
  public password: string | null;

  constructor(
    customerId: number,
    customerFirstName: string,
    customerLastName: string,
    address: string,
    phone: number,
    email: string,
    password: string
  ) {
    this.customerId = customerId;
    this.customerFirstName = customerFirstName;
    this.customerLastName = customerLastName;
    this.address = address;
    this.phone = phone;
    this.email = email;
    this.password = password;
  }
}
