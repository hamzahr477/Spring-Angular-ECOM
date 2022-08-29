// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const data = {
  payementMethod:  [
    {value: 'COD', viewValue: 'Cash on delivery'},
    {value: 'Paypal', viewValue: 'Paypal'},
    {value: 'CreditCard', viewValue: 'Credit Card'},
  ],

  getLablePaymentMethod(key:string){
    return this.payementMethod.find(value => value.value==key)!.viewValue
  }

};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.
