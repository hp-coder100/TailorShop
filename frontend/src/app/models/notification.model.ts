export class Notification {
    public notificationId: number;
    public message: string;
    public customerId: number;
    public shopId: number;

    constructor(notificationId: number, message: string, customerId: number, shopId: number) {
        this.notificationId = notificationId || 0;
        this.message = message || '';
        this.customerId = customerId || 0;
        this.shopId = shopId || 0;
    }
}