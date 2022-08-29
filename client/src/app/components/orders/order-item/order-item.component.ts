import { Component, Input,OnInit } from '@angular/core';
import {Order} from "../../../model/order.model";
import {AddModifyProductComponent} from "../../products/add-modify-product/add-modify-product.component";
import {ModifyOrderComponent} from "../modify-order/modify-order.component";
import {MatSnackBar} from "@angular/material/snack-bar";
import {CartService} from "../../../service/cart.service";
import {MatDialog} from "@angular/material/dialog";
import {data } from "../../../static-data/data";

@Component({
  selector: 'tr[app-order-item]',
  templateUrl: './order-item.component.html',
  styleUrls: ['./order-item.component.scss']
})
export class OrderItemComponent implements OnInit {

  @Input() order: Order | undefined
  constructor(private _snackBar: MatSnackBar,public cartService: CartService,public dialog: MatDialog) { }

  ngOnInit(): void {
  }
  payementMethodLable(){
    return data.getLablePaymentMethod(this.order?.payementMethod!)
  }


  view() {
    const dialogRef = this.dialog.open(ModifyOrderComponent, {
      disableClose: true,
      width: '650px',
    });
    dialogRef.componentInstance.order = this.order;

    dialogRef.afterClosed().subscribe(result => {
      if(!result.error && result?.order != null){
        this.order = result.order
      }
    });
  }
}
