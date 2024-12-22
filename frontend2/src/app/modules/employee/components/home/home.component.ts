import { Component } from '@angular/core';
import {DashboardService} from '../../../../services/dashboard/dashboard.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  email: string = '';
  dashboardData: any;


  constructor(private dashboardService: DashboardService) {
  }

  ngOnInit(): void {
    this.email = localStorage.getItem('user_email') ?? '';
    this.getDashboardSummaryForEmployee(this.email);

  }

  getDashboardSummaryForEmployee(email: string) {
    this.dashboardService.getDashboardSummaryForEmployee(email).subscribe(
      (data) => {
        this.dashboardData = data;
        console.log(this.dashboardData);
      },
      (error) => {
        console.error('Error fetching manager summary', error);
      }
    );
  }
}
