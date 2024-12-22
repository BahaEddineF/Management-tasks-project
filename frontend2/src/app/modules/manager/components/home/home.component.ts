import {Component, OnInit} from '@angular/core';
import {DashboardService} from '../../../../services/dashboard/dashboard.service';

@Component({
  selector: 'app-home2',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit{
  email: string = '';
  dashboardData: any;
  constructor(private dashboardService: DashboardService) {
  }

  ngOnInit(): void {
    this.email = localStorage.getItem('user_email') ?? '';
    this.getDashboardSummaryForManager(this.email);
  }

  getDashboardSummaryForManager(email: string) {
    this.dashboardService.getDashboardSummaryForManager(email).subscribe(
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
