import { Component, computed, signal } from '@angular/core';

@Component({
  selector: 'app-employee-dashboard',
  templateUrl: './employee-dashboard.component.html',
  styleUrl: './employee-dashboard.component.scss'
})
export class EmployeeDashboardComponent {
  collapsed = signal(false)

  sidenavWidth = computed(()=> this.collapsed() ? '65px' : '250px');
}
