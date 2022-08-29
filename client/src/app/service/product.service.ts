import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {Product} from "../model/product.model";

@Injectable({providedIn:"root"})
export class ProductService{


  constructor(private http:HttpClient) {

  }

  //Get All Product with search and pagination
  searchAllProducts(offset:number,pageSize:number,sortField:string,sortType:string, search:string):Observable<any>{
    let params = new HttpParams()
      .set('pageSize', String(pageSize))
      .set('sortType', sortType)
      .set('sortField', sortField)
      .set('offset', String(offset));
    params = search != null && search!="" ? params.set('search', search) : params;
    return this.http.get<any>(environment.host+"/articles/search",{params}).pipe(
      map(data => ({content : data.content, totalElements : data.totalElements ,number:data.numberOfElements}))
    );
  }

  //Get all products
  getAllProducts():Observable<any>{
    return this.http.get<any>(environment.host+"/articles").pipe(
      map(data => ({content : data}))
    );
  }
  //Get Product by ID
  getProductById(id:number){
    return this.http.get<any>(environment.host+"/articles/"+id).pipe(
      map(data => ({content : data}))
    );
  }
  //Save Product
  saveProduct(product:Product):Observable<any>{
    return this.http.post<any>(environment.host+"/articles", product).pipe(
      map(data => ({content : data}))
    );
  }
 //Save Image
  saveImage( image : File,id:number):Observable<any>{
    const formData = new FormData();
    image?formData.append("image", image, image.name):null
    formData.append("id",''+id)
    return this.http.post<any>(environment.host+"/articles/saveImage", formData).pipe(
      map(data => ({content : data}))
    );
  }
  //Edit Product
    editProduct(product:Product, image : File):Observable<any>{

    return this.http.put<any>(environment.host+"/articles", product).pipe(
      map(data => ({content : data}))
    );
  }

}
