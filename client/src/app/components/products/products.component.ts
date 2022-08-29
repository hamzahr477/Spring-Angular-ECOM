import { Component, OnInit } from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {AddModifyProductComponent} from "./add-modify-product/add-modify-product.component";
import {ProductService} from "../../service/product.service";
import {catchError, map, Observable, of, startWith} from "rxjs";
import {Product} from "../../model/product.model";
import {DataState} from "../../service/data.state";
import {ActivatedRoute, Router} from "@angular/router";
import { tap } from 'rxjs/operators';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss']
})
export class ProductsComponent implements OnInit {
  products$ : Observable<DataState<Product[]>> |null=null
  public offset: number=1;
  public pageSize : number=12;
  public sortField : string="id";
  public sortType : string="asc";
  public search: string  = "";
  public etat: string = "all";
  public famille: string= "all";

  constructor(private _router: Router,private route:  ActivatedRoute,public dialog: MatDialog,private productService:ProductService) {
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(AddModifyProductComponent, {
      disableClose: true,
      width: '600px',
      data: {title: "Add Product"},
    });

    dialogRef.afterClosed().subscribe(result => {
      if(!result.error)
        this.getData()
    });
  }
  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.offset = !!params['offset'] ? params['offset'] : this.offset;
      this.pageSize =!!params['pageSize'] ? params['pageSize']: this.pageSize;
      this.sortField =!!params['sortField'] ? params['sortField'] : this.sortField;
      this.sortType = !!params['sortType']? params['sortType'] : this.sortType;
      this.search = !!params['search'] ? params['search'] : "";
      this.getData();
    });
  }

  public getData() {
    this.products$ = this.productService.searchAllProducts(this.offset-1,this.pageSize,this.sortField,this.sortType,this.search).pipe(
      map(data=>({dataState : "LOADED",data : data.content ,totalElements :data.totalElements ,number:data.number })),
      startWith({dataState:"LOADING"}),
      catchError(err=>of({dataState:"ERROR",errorMessage:err.message}))
    );
  }

  public onPageChange = (pageNumber: number) => {
    if(this.offset != pageNumber) {this.offset = pageNumber; this.navigateToGE();
    }
  }

  navigateToGE(){
    this._router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        offset : this.offset,
        sortType :   this.sortType,
        sortField : this.sortField,
        search :   this.search,
      },
      queryParamsHandling: 'merge',
      // preserve the existing query params in the route
      skipLocationChange: false
      // do not trigger navigation
    });
    this.getData();
  }
}
