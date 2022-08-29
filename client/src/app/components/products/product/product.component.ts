import {Component, Input, OnInit} from '@angular/core';
import {AddModifyProductComponent} from "../add-modify-product/add-modify-product.component";
import {MatDialog} from "@angular/material/dialog";
import {Product} from "../../../model/product.model";
import {CartService} from "../../../service/cart.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {environment} from "../../../../environments/environment";

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss']
})
export class ProductComponent implements OnInit {
  @Input() product: Product | undefined;
  host = environment.host


  constructor(private _snackBar: MatSnackBar,public cartService: CartService,public dialog: MatDialog) { }

  ngOnInit(): void {
  }

  edit() {
    const dialogRef = this.dialog.open(AddModifyProductComponent, {
      disableClose: true,
      width: '600px',
      data: {title: "Add Product"},
    });
    dialogRef.componentInstance.product_ = this.product;

    dialogRef.afterClosed().subscribe(result => {
      if(!result.error && result?.product != null){
        this.product = result.product
      }
    });
  }

  addToCart() {
    this.cartService.addToCart(this.product!)
  }
}
