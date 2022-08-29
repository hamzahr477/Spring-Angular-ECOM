import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterModule, Routes} from "@angular/router";
import {HomeComponent} from "./home/home.component";
import {ProductsComponent} from "./products/products.component";
import {OrdersComponent} from "./orders/orders.component";
import {NavBarComponent} from "./nav-bar/nav-bar.component";
import {PanierComponent} from "./panier/panier.component";
import {NgbAlertModule, NgbCollapseModule, NgbPaginationModule} from "@ng-bootstrap/ng-bootstrap";
import { ProductComponent } from './products/product/product.component';
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import { AddModifyProductComponent } from './products/add-modify-product/add-modify-product.component';
import { ModifyOrderComponent } from './orders/modify-order/modify-order.component';
import { PanierItemComponent } from './panier/panier-item/panier-item.component';
import { MatDialogModule } from '@angular/material/dialog';
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {FormsModule, ReactiveFormsModule } from '@angular/forms';
import {MatSnackBar, MatSnackBarModule} from "@angular/material/snack-bar";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {MatBadgeModule} from "@angular/material/badge";
import {MatSelectModule} from "@angular/material/select";
import { OrderItemComponent } from './orders/order-item/order-item.component';
import {MatButtonToggleModule} from "@angular/material/button-toggle";

const routes: Routes = [

  {
    path: 'home',
    component: HomeComponent
  },
  {
    path: 'products',
    component: ProductsComponent
  },{
    path: 'orders',
    component: OrdersComponent
  },{
    path: 'panier',
    component: PanierComponent
  },{
    path: '**',
    redirectTo:'home'
  }
];

// @ts-ignore
@NgModule({
  declarations: [
    HomeComponent,
    NavBarComponent,
    PanierComponent,
    ProductsComponent,
    OrdersComponent,
    ProductComponent,
    AddModifyProductComponent,
    ModifyOrderComponent,
    PanierItemComponent,
    OrderItemComponent
  ],
  exports: [
    NavBarComponent,
    RouterModule
  ],
  imports: [
    MatSnackBarModule,
    MatDialogModule,
    RouterModule.forChild(routes),
    CommonModule,
    RouterModule,
    NgbCollapseModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    NgbAlertModule,
    NgbPaginationModule,
    ReactiveFormsModule,
    FormsModule,
    MatProgressSpinnerModule,
    MatBadgeModule,
    MatSelectModule,
    MatButtonToggleModule,
  ]
})

export class ComponentModule { }
