import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {HomeComponent} from "./home/home.component";
import {AddItemComponent} from "./add-item/add-item.component";
import {UpdateItemComponent} from "./update-item/update-item.component";
import {LoginComponent} from "./login/login.component";
import {AuthGuardService} from "./services/auth-guard.service";

const routes: Routes = [
  { path: 'login', component: LoginComponent},
  { path: '', component: HomeComponent, canActivate: [AuthGuardService]},
  { path: 'add-item', component: AddItemComponent, canActivate: [AuthGuardService]},
  { path: 'update-item/:id', component: UpdateItemComponent, canActivate: [AuthGuardService]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
