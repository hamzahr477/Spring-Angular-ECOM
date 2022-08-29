import {DetailsOrder} from "./detailsOrder.model";


export interface Order{

  id?: number;
  numOrder?: string;
  total?: number;
  productCommandeList?:DetailsOrder[];
  payementMethod?: string;
  orderDate? : Date

}
