import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { VerifyEmailComponent } from './components/verify-email/verify-email.component';
import { authGuard } from './guards/auth.guard';

const routes: Routes = [
  {path: 'login', component:LoginComponent},
  {path: 'verify', component:VerifyEmailComponent},
  {path: '', redirectTo: 'login',pathMatch:'full'},
  {
    path: 'admin',
    canActivate: [authGuard],
    data: {role:'admin'},
    loadChildren: ()=> import('./modules/admin/admin.module').then((m)=>m.AdminModule)
  },
  {
    path: 'manager',
    canActivate: [authGuard],
    data: {role:'manager'},
    loadChildren: ()=> import('./modules/manager/manager.module').then((m)=>m.ManagerModule)
  },
  {
    path: 'employee',
    canActivate: [authGuard],
    data: {role:'employee'},
    loadChildren: ()=> import('./modules/employee/employee.module').then((m)=>m.EmployeeModule)
  },
  {path: '**', component: NotFoundComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
