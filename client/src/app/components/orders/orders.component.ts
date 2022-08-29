import { Component, OnInit } from '@angular/core';
import {OrderService} from "../../service/order.service";
import {catchError, map, Observable, of, startWith} from "rxjs";
import {DataState} from "../../service/data.state";
import {Order} from "../../model/order.model";

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.scss']
})
export class OrdersComponent implements OnInit {

  public orders$ : Observable<DataState<Order[]>> | undefined
  constructor(public orderService : OrderService) { }

  ngOnInit(): void {
   this.getData()
  }
  getData(){
    this.orders$ = this.orderService.getAllOrders().pipe(
      map(data=>({dataState : "LOADED",data : data.content})),
      startWith({dataState:"LOADING"}),
      catchError(err=>of({dataState:"ERROR",errorMessage:err.message}))
    )
  }

}
