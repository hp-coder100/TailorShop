export class Payment {
    public paymentId: number;
    public amount: number;
    public paymentDate: Date;
    public status: string;
    public appointmentId: number;

    constructor(paymentId: number, amount: number, paymentDate: Date, status: string, appointmentId: number) {
        this.paymentId = paymentId || 0;
        this.amount = amount  || 0;
        this.paymentDate = paymentDate || null;
        this.status = status || '';
        this.appointmentId = appointmentId || 0;
    }
}