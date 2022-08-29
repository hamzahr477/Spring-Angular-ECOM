import {Component, Input, OnInit} from '@angular/core';
import {ProductService} from "../../../service/product.service";
import {catchError, map, Observable, of, startWith} from "rxjs";
import {DataState} from "../../../service/data.state";
import {Product} from "../../../model/product.model";
import {CartService} from "../../../service/cart.service";
import {environment} from "../../../../environments/environment";

@Component({
  selector: 'app-panier-item',
  templateUrl: './panier-item.component.html',
  styleUrls: ['../panier.component.scss']
})
export class PanierItemComponent implements OnInit {


  @Input() product:Product | undefined;
  @Input() quantity:number = 0;
  host = environment.host;


  constructor(public cartService:CartService,public productService : ProductService) { }

  ngOnInit(): void {
  }

  sub() {
    this.cartService.subQuantity(this.product?.id!)
  }
  add(){
    this.cartService.addToCart(this.product!)
  }
}
