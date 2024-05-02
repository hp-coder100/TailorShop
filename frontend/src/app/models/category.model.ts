export class Category {
  public categoryId: number;
  public categoryName: string;
  public details: string;
  public shopId: number;
  public imageUrl: string = '../../assets/tailorshop-3.jpg';

  constructor(
    categoryId: number,
    categoryName: string,
    details: string,
    shopId: number
  ) {
    this.categoryId = categoryId;
    this.categoryName = categoryName;
    this.details = details;
    this.shopId = shopId;
  }
}
