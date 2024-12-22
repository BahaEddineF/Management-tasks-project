import { Component } from '@angular/core';
import {DashboardService} from '../../../../services/dashboard/dashboard.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  dashboardData: any;

  constructor(private dashboardService: DashboardService) {}

  ngOnInit(): void {
    this.fetchDashboardSummary();
  }

  fetchDashboardSummary(): void {
    this.dashboardService.getDashboardSummaryForAdmin().subscribe({
      next: (data) => {
        this.dashboardData = data;
        console.log(this.dashboardData); // Log the data for debugging
      },
      error: (err) => {
        console.error('Error fetching dashboard summary:', err);
      }
    });
  }
}
