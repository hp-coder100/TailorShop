export class Tailor {
  public shopId: number;
  public tailorName: string;
  public description: string;
  public email: string;
  public password: string | null;
  public coverUrl: string = '../../assets/tailorshop-7.jpg';

  constructor(
    shopId: number,
    tailorName: string,
    description: string,
    email: string,
    password: string
  ) {
    this.shopId = shopId || 0;
    this.tailorName = tailorName || '';
    this.description = description || '';
    this.email = email || '';
    this.password = password || '';
  }
}
