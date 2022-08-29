import {Component, ElementRef, Inject, Input, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Product} from "../../../model/product.model";
import {FormGroup} from "@angular/forms";
import { FormControl,Validators } from '@angular/forms';
import {ProductService} from "../../../service/product.service";
import {catchError, map, Observable, of, startWith} from 'rxjs';
import {MatSnackBar} from "@angular/material/snack-bar";
import {DataState} from "../../../service/data.state";
import {environment} from "../../../../environments/environment";


@Component({
  selector: 'app-add-modify-product',
  templateUrl: './add-modify-product.component.html',
  styleUrls: ['./add-modify-product.component.scss']
})
export class AddModifyProductComponent implements OnInit {
  @Input() product_: Product | undefined;
  image : File | undefined;
  imgSrc : string | ArrayBuffer | null = '';
  public productForm: FormGroup | undefined;
  product: Product = {}
  public loading : boolean = false;
  imageInput: ElementRef | undefined;
  host = environment.host
  constructor(private _snackBar: MatSnackBar,public productService:ProductService,public dialogRef: MatDialogRef<AddModifyProductComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit(): void {
    this.productForm= new FormGroup({
      name: new FormControl(this.product_?.name === undefined?'' : this.product_?.name, [Validators.required, Validators.maxLength(100)]),
      description: new FormControl(this.product_?.description === undefined?'' : this.product_?.description),
      quantity: new FormControl(this.product_?.quantity === undefined?0 : this.product_?.quantity, [Validators.required, Validators.min(0),Validators.pattern("^[0-9]*$")]),
      price: new FormControl(this.product_?.price === undefined?0 : this.product_?.price, [Validators.required, Validators.min(0)]),
    })
  }



  public hasError = (controlName: string, errorName: string) =>{
    return this.productForm!.controls[controlName].hasError(errorName);
  }
  public createProduct = (productFormValue: any) => {
    if (this.productForm!.valid) {
      this.loading = true;
      this.executeProductCreation(productFormValue);
      let productRes:Observable<DataState<Product>>
      if(this.product_?.id === undefined){
        productRes= this.productService.saveProduct(this.product).pipe(
          map(data=>({dataState : "LOADED",data : data.content  })),
          startWith({dataState:"LOADING"}),
          catchError(err=>of({dataState:"ERROR",errorMessage:err.error.message}))
        );
      }else{
        productRes = this.productService.editProduct(this.product, this.image!).pipe(
          map(data=>({dataState : "LOADED",data : data.content  })),
          startWith({dataState:"LOADING"}),
          catchError(err=>of({dataState:"ERROR",errorMessage:err.error.message}))
        );
      }
      productRes.subscribe(res=>{
        this.loading = !(res.dataState == 'LOADED' || res.dataState == 'ERROR');
        res.dataState == 'ERROR' ? this._snackBar.open(res.errorMessage!=""?res.errorMessage:"Error", "close",{
          duration: 5000,
          verticalPosition:'top',
          horizontalPosition:'right'}): null
        if(res.dataState == 'LOADED') {
          this.product_ = res.data
          this.loading=true
          if(this.image != undefined ){
            let productImage:Observable<DataState<any>> =  this.productService.saveImage(this.image!,res.data?.id!).pipe(
              map(data=>({dataState : "LOADED",data : data.content  })),
              startWith({dataState:"LOADING"}),
              catchError(err=>of({dataState:"ERROR",errorMessage:err}))
            );
            productImage.subscribe(value => {
              console.log(value);
              this.loading = !(value.dataState == 'LOADED' || value.dataState == 'ERROR');
              value.dataState == 'ERROR' ? this._snackBar.open("Error : Cannot save image", "close",{
                duration: 5000,
                verticalPosition:'top',
                horizontalPosition:'right'}): null
              if(value.dataState == 'LOADED'){
                this._snackBar.open(this.product_?.id === undefined?"Product successfully added":"Product successfully modified", "close",{
                  duration: 5000,
                  verticalPosition:'top',
                  horizontalPosition:'right'})
                res.data!.urlImg = value.data?.urlImg
                this.dialogRef.close({error:false,product:res.data})
              }
            })
          }else this.dialogRef.close({error:false,product:res.data})

        }
      })
    }

  }
  private executeProductCreation = (productFormValue : any) => {
    this.product = {
      id:this.product_?.id,
      name: productFormValue.name,
      description: productFormValue.description,
      price: +productFormValue.price,
      quantity: +productFormValue.quantity,
      urlImg:this.product_?.urlImg
    }
  }

  onImageChange(event : any) {
    this.image = event?.target?.files[0];
    const reader = new FileReader();
    reader.onload = e => this.imgSrc = reader.result
    reader!.readAsDataURL(this.image!);
  }
}
