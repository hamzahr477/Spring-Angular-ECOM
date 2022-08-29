import {Component, Inject, Input, OnInit} from '@angular/core';
import {Product} from "../../../model/product.model";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {ProductService} from "../../../service/product.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Order} from "../../../model/order.model";
import {DetailsOrder} from "../../../model/detailsOrder.model";
import {data} from "../../../static-data/data";
import {catchError, map, Observable, of, startWith} from "rxjs";
import {DataState} from "../../../service/data.state";
import {OrderService} from "../../../service/order.service";
import {environment} from "../../../../environments/environment";

@Component({
  selector: 'app-modify-order',
  templateUrl: './modify-order.component.html',
  styleUrls: ['./modify-order.component.scss']
})
export class ModifyOrderComponent implements OnInit {
  @Input() order: Order | undefined;
  newOrder : Order | undefined;
  public productForm: FormGroup | undefined;
  public loading : boolean = false;
  isChanged: boolean = false;
  payementMethods: any = data.payementMethod ;
  public orderForm: FormGroup | undefined;
  host = environment.host;

  getLimit(productId : number){
    let index =this.order?.productCommandeList?.findIndex(value => value.product?.id! == productId);
    if(index != -1)
      return this.order!.productCommandeList![index!].quantity!+this.order?.productCommandeList![index!].product?.quantity!
    return 0
  }
  constructor(private orderService:OrderService,private _snackBar: MatSnackBar,public productService:ProductService,public dialogRef: MatDialogRef<ModifyOrderComponent>,
              @Inject(MAT_DIALOG_DATA) public data_dialog: any) { }

  ngOnInit(): void {
    this.newOrder =JSON.parse(JSON.stringify(this.order))
    this.orderForm= new FormGroup({
      payementMethod: new FormControl(this.newOrder?.payementMethod, [Validators.required]),
    })
  }
  add(item: DetailsOrder) {
    this.isChanged=true
    if((item.quantity!+1)>this.getLimit(item.product?.id!))
      this._snackBar.open( "you have reached the maximum quantity for product "+item.product?.name,"close",{
        duration : 3000,
        horizontalPosition:"right",
        verticalPosition : "top"
      })
    else {
      item.quantity! += 1
    }
  }


  sub(item: DetailsOrder){
    this.isChanged=true
    if(item.quantity! - 1 == 0){
      let index =this.order?.productCommandeList?.findIndex(value => value.id == item.id);
      this.newOrder?.productCommandeList?.splice(index!,1)
    } else item.quantity!-=1
  }

  remove(item: DetailsOrder) {
    this.isChanged=true
    let index =this.order?.productCommandeList?.findIndex(value => value.id == item.id);
    this.newOrder?.productCommandeList?.splice(index!,1)
  }
  save(productFormValue: any) {
    if (this.orderForm!.valid) {
      this.loading = true
      this.executeProductCreation(productFormValue);
      let orderRes:Observable<DataState<Order>> = this.orderService.editOrder(this.newOrder!).pipe(
        map(data=>({dataState : "LOADED",data : data.content  })),
        startWith({dataState:"LOADING"}),
        catchError(err=>of({dataState:"ERROR",errorMessage:err.error.message}))
      );
      orderRes.subscribe(res=>{
        this.loading = !(res.dataState == 'LOADED' || res.dataState == 'ERROR');
        res.dataState == 'ERROR' ? this._snackBar.open(res.errorMessage!=""?res.errorMessage:"Error", "close",{
          duration: 5000,
          verticalPosition:'top',
          horizontalPosition:'right'}): null
        if(res.dataState == 'LOADED') {
          this.dialogRef.close({error:false,order:res.data})
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
    this.newOrder!.payementMethod = productFormValue.payementMethod
  }
}
