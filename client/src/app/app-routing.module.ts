import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ComponentModule} from "./components/component.module";

const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: '',
        loadChildren: () => import('./components/component.module').then(module => module.ComponentModule)
      }
    ]
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
