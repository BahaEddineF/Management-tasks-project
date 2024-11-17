import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { EmployeeRoutingModule } from './employee-routing.module';
import { HomeComponent } from './components/home/home.component';
import { EmployeeDashboardComponent } from './components/employee-dashboard/employee-dashboard.component';
import { TasksComponent } from './components/tasks/tasks.component';
import { ProjectsComponent } from './components/projects/projects.component';
import { CustomSidenavComponent } from './components/custom-sidenav/custom-sidenav.component';


import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatDialogModule} from '@angular/material/dialog';
import {MatInputModule} from '@angular/material/input';
import {MatSelectModule} from '@angular/material/select';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatTableModule} from '@angular/material/table';
import {MatPaginatorModule } from '@angular/material/paginator';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSortModule } from '@angular/material/sort';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatListModule} from '@angular/material/list';
import { AdminRoutingModule } from '../admin/admin-routing.module';
import { EditStatusComponent } from './components/tasks/edit-status/edit-status.component';




@NgModule({
  declarations: [
    HomeComponent,
    EmployeeDashboardComponent,
    TasksComponent,
    ProjectsComponent,
    CustomSidenavComponent,
    EditStatusComponent
  ],
  imports: [
    CommonModule,
    EmployeeRoutingModule,
    CommonModule,
    AdminRoutingModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatDialogModule,
    MatInputModule,
    MatSelectModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    MatSidenavModule,
    MatListModule
  ]
})
export class EmployeeModule { }
