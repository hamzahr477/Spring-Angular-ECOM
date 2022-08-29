import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddModifyProductComponent } from './add-modify-product.component';

describe('AddModifyProductComponent', () => {
  let component: AddModifyProductComponent;
  let fixture: ComponentFixture<AddModifyProductComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddModifyProductComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddModifyProductComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
