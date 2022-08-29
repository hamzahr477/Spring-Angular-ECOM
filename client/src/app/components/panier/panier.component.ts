import { Component, OnInit } from '@angular/core';
import {CartService} from "../../service/cart.service";
import {catchError, map, Observable, of, startWith} from "rxjs";
import {DataState} from "../../service/data.state";
import {Product} from "../../model/product.model";
import {ProductService} from "../../service/product.service";
import {DetailsOrder} from "../../model/detailsOrder.model";
import {OrderService} from "../../service/order.service";
import {FormControl, FormGroup, Validators, ɵFormGroupValue, ɵTypedOrUntyped} from "@angular/forms";
import {Order} from "../../model/order.model";
import {MatSnackBar} from "@angular/material/snack-bar";
import {data} from "../../static-data/data";

@Component({
  selector: 'app-panier',
  templateUrl: './panier.component.html',
  styleUrls: ['./panier.component.scss']
})
export class PanierComponent implements OnInit {

  public error :boolean = false;
  public checkOutLoading : boolean = false;
  public  loading : boolean = this.cartService.items.length!=0;
  public product$: Observable<DataState<Product>> | undefined
  public orderForm: FormGroup | undefined;
  public order: Order | undefined;
  payementMethods: any = data.payementMethod ;

  constructor(private _snackBar: MatSnackBar,private orderService: OrderService ,public cartService:CartService,public productService:ProductService) { }

  ngOnInit(): void {
    this.getData()
    this.orderForm= new FormGroup({
      payementMethod: new FormControl({value:'',disabled:this.cartService.items?.length == 0}, [Validators.required]),
     })
  }
  getData(){
    this.error = false;
    for(let items of this.cartService.items){
      if(!this.error && !isNaN(+items.product?.id!) ){
        this.product$= this.productService.getProductById(items.product?.id!).pipe(
          map(data=>({dataState : "LOADED",data : data.content })),
          startWith({dataState:"LOADING"}),
          catchError(err=>of({dataState:"ERROR",errorMessage:err}))
        )
        this.product$.subscribe(value => {
          if(value.dataState == "LOADING")
            this.loading = true
          if(value.dataState == "ERROR") {
            if (value.errorMessage?.error?.code == "RNF"){
              this.cartService.removeFromCart(items.product?.id!)
            }
            else
              this.error = true
            this.loading = false
          }
          else if(value.dataState == "LOADED"){
            this.cartService.repalce({product:value.data! , quantity : items.quantity})
            this.loading = false
          }
        })

      }else if(isNaN(+items.product?.id!)){
        this.cartService.removeFromCart(items.product?.id!)
      }
    }
  }

  getTotal() {
    let total = 0
    for(let item of this.cartService.items!)
      total += item.quantity! * item.product?.price!
    return total
  }

  checkout(productFormValue: any) {
    if (this.orderForm!.valid) {
      this.checkOutLoading = true
      this.executeProductCreation(productFormValue);
      let orderRes:Observable<DataState<Order>> = this.orderService.saveOrder(this.order!).pipe(
        map(data=>({dataState : "LOADED",data : data.content  })),
        startWith({dataState:"LOADING"}),
        catchError(err=>of({dataState:"ERROR",errorMessage:err.error.message}))
      );
      orderRes.subscribe(res=>{
        this.checkOutLoading = !(res.dataState == 'LOADED' || res.dataState == 'ERROR');
        res.dataState == 'ERROR' ? this._snackBar.open(res.errorMessage!=""?res.errorMessage:"Error", "close",{
          duration: 5000,
          verticalPosition:'top',
          horizontalPosition:'right'}): null
        if(res.dataState == 'LOADED') {
          this.cartService.clearCart()
          this._snackBar.open("Order successfully added", "close",{
            duration: 5000,
            verticalPosition:'top',
            horizontalPosition:'right'})
        }
      })
    }
  }

  public hasError = (controlName: string, errorName: string) =>{
    return this.orderForm!.controls[controlName].hasError(errorName);
  }

  private executeProductCreation(productFormValue: any) {
    this.order = {
      payementMethod : productFormValue.payementMethod,
      productCommandeList : this.cartService.items
    }
  }
}
