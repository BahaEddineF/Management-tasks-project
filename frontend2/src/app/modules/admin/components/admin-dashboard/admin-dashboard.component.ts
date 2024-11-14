import { Component, computed, signal } from '@angular/core';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.scss'
})
export class AdminDashboardComponent {

    collapsed = signal(false)

    sidenavWidth = computed(()=> this.collapsed() ? '65px' : '250px');
}
