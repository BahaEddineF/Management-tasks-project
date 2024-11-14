import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ManagerDashboardComponent } from './components/manager-dashboard/manager-dashboard.component';
import { TasksComponent } from './components/tasks/tasks.component';
import { HomeComponent } from './components/home/home.component';
import { ProjectsComponent } from './components/projects/projects.component';

const routes: Routes = [
    { path: '', component: ManagerDashboardComponent,
      children:[
        {path:'home', component:HomeComponent},
        {path:'tasks',component:TasksComponent },
        {path:'projects', component:ProjectsComponent},
        {path: '',redirectTo:'/manager/home', pathMatch:'full'}
      ]
    },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ManagerRoutingModule { }
