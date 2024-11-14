import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { UsersComponent } from './components/users/users.component';
import { ProjectsComponent } from './components/projects/projects.component';
import { HomeComponent } from './components/home/home.component';

const routes: Routes = [
  { path: '', component: AdminDashboardComponent,
    children:[
      {path:'home', component:HomeComponent},
      {path:'users',component:UsersComponent },
      {path:'projects', component:ProjectsComponent},
      {path: '',redirectTo:'/admin/home', pathMatch:'full'}
    ]},


];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
