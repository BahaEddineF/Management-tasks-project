import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private baseUrl = 'http://localhost:4200/api';  // Backend URL (adjust to your backend's URL)

  constructor(private http: HttpClient) {}

  /**
   * Fetches the dashboard summary for an admin.
   * @returns An Observable of the summary object.
   */
  getDashboardSummaryForAdmin(): Observable<any> {
    return this.http.get(`${this.baseUrl}/dashboard/admin/summary`);
  }

  /**
   * Fetches the dashboard summary for a manager based on email.
   * @param email The manager's email.
   * @returns An Observable of the summary object.
   */
  getDashboardSummaryForManager(email: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/dashboard/manager/summary/${email}`);
  }

  /**
   * Fetches the dashboard summary for an employee based on email.
   * @param email The employee's email.
   * @returns An Observable of the summary object.
   */
  getDashboardSummaryForEmployee(email: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/dashboard/employee/summary/${email}`);
  }
}
