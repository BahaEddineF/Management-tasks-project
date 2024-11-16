import { Component, computed, signal } from '@angular/core';

@Component({
  selector: 'app-manager-dashboard',
  templateUrl: './manager-dashboard.component.html',
  styleUrl: './manager-dashboard.component.scss'
})
export class ManagerDashboardComponent {

  collapsed = signal(false)

  sidenavWidth = computed(()=> this.collapsed() ? '65px' : '250px');
}
