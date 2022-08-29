import { Injectable } from '@angular/core';
import {DetailsOrder} from "../model/detailsOrder.model";
import {Product} from "../model/product.model";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Order} from "../model/order.model";

@Injectable({
  providedIn: 'root'
})
export class CartService {

  items:DetailsOrder[] = [];

  constructor(private _snackBar: MatSnackBar) {
    this.items = JSON.parse(localStorage.getItem('cart') || '[]');
  }

  removeFromCart(id:number){
    let index = this.items.findIndex(value => value.product?.id == id)
    if(index != -1)
      this.items.splice(index,1)
    localStorage.setItem("cart",JSON.stringify(this.items))
  }

  addToCart(product: Product) {
    if (product?.quantity! == 0)
      this._snackBar.open( "Product "+product?.name+" is currently out of stock.","close",{
        duration : 3000,
        horizontalPosition:"right",
        verticalPosition : "top"
      })
    else if((this.getItemQuantity(product?.id!)!+1)>product?.quantity!)
      this._snackBar.open( "you have reached the maximum quantity for product "+product?.name,"close",{
        duration : 3000,
        horizontalPosition:"right",
        verticalPosition : "top"
      })
    else {
      let index = this.items.findIndex(value => value.product?.id == product.id)
      if (index != -1)
        this.items[index].quantity = this.items[index].quantity! + 1
      else this.items.push({product: product, quantity: 1});
      localStorage.setItem("cart", JSON.stringify(this.items))
      this._snackBar.open("Product " + product.name + " has been added to cart", "close", {
        duration: 3000,
        horizontalPosition: "right",
        verticalPosition: "top"
      })
    }

  }

  getItems() {
    return this.items;
  }

  getItemQuantity(id:number){
    let index = this.items.findIndex(value => value.product?.id == id)
    if(index != -1)
      return this.items[index].quantity
    return 0
  }

  subQuantity(id:number){
    let index = this.items.findIndex(value => value.product?.id == id)
    if(index != -1)
      this.items[index].quantity = this.items[index].quantity! - 1
    if(this.items[index].quantity==0)
      this.items.splice(index,1)
    localStorage.setItem("cart",JSON.stringify(this.items))
  }

  getItemsCount(){
    let count = 0;
    this.items.map(value => {count+=value.quantity!;});
    return count>0 ? count : ""
  }

  clearCart() {
    this.items = [];
    localStorage.setItem("cart",JSON.stringify(this.items))
    return this.items;
  }

  repalce(orderDetails: DetailsOrder) {
    let index = this.items.findIndex(value => value.product?.id == orderDetails?.product?.id)
    if(index != -1) {
      if (orderDetails.quantity! < 1)
        this.items.splice(index, 1)
      else this.items[index] = orderDetails
    }
    localStorage.setItem("cart",JSON.stringify(this.items))
  }
}
