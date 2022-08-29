import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {Product} from "../model/product.model";
import {Order} from "../model/order.model";

@Injectable({providedIn:"root"})
export class OrderService{



  constructor(private http:HttpClient) {

  }


  //Get all orders
  getAllOrders():Observable<any>{
    return this.http.get<any>(environment.host+"/orders").pipe(
      map(data => ({content : data}))
    );
  }

  //Save Product
  saveOrder(order:Order):Observable<any>{
    return this.http.post<any>(environment.host+"/orders", order).pipe(
      map(data => ({content : data}))
    );
  }
  //Edit Product
  editOrder(order:Order):Observable<any>{
    return this.http.put<any>(environment.host+"/orders/", order).pipe(
      map(data => ({content : data}))
    );
  }

}
