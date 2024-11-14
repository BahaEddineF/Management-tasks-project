import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { EmployeeDashboardComponent } from './components/employee-dashboard/employee-dashboard.component';
import { HomeComponent } from './components/home/home.component';
import { TasksComponent } from './components/tasks/tasks.component';
import { ProjectsComponent } from './components/projects/projects.component';

const routes: Routes = [

  { path: '', component: EmployeeDashboardComponent,
    children:[
      {path:'home', component:HomeComponent},
      {path:'tasks',component:TasksComponent },
      {path:'projects', component:ProjectsComponent},
      {path: '',redirectTo:'/employee/home', pathMatch:'full'}
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EmployeeRoutingModule { }
