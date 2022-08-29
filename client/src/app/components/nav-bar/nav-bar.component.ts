import {Component, NgZone, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {CartService} from "../../service/cart.service";

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.scss']
})
export class NavBarComponent implements OnInit {
  public isCollapsed = true;
  public searchKey: String = "";
  cartItemsCount: any;
  constructor(public cartService:CartService,public router:Router,private ngZone:NgZone) { }

  ngOnInit(): void {
  }

  search() {
    if(this.searchKey != "") {
      this.router.navigate(['products'], {queryParams: {search: this.searchKey}});
    }else{
      this.router.navigate(['products']);
    }
  }
}
