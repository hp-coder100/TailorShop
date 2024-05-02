export class Measurement {
    public measurementId: number;
    public details: string;
    public appointmentId: number;
    public customerId: number;
    public shopId: number;

    constructor(measurementId: number, details: string, appointmentId: number, customerId: number, shopId: number) {
        this.measurementId = measurementId || 0;
        this.details = details || '';
        this.appointmentId = appointmentId || 0;
        this.customerId = customerId || 0;
        this.shopId = shopId || 0;
    }
}