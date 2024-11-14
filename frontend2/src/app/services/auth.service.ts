import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl = 'http://localhost:4200/api/v1/auth/';  // Replace with your backend API URL




  constructor(private http: HttpClient, private router: Router) { }

  setToken(access_token: string, refresh_token: string): void {
    localStorage.setItem('access_token', access_token);
    localStorage.setItem('refresh_token', refresh_token);
  }
  setUserRole(){
    localStorage.setItem('user_role',this.getDecodedToken().role)
  }

  private decodeToken(token: string): any {
    const payload = token.split('.')[1];
    return JSON.parse(atob(payload));
  }

  getDecodedToken(): any {
    const token = this.getAccessToken();
    
    if (!token) {
      console.error('No token found. Cannot decode.');
      return null; // or throw an error if preferred
    }
    
    return this.decodeToken(token);
  }


  
  getAccessToken(): string | null {
    return localStorage.getItem('access_token');
  }
  getRefreshToken(): string | null {
    return localStorage.getItem('refresh_token');
  }

  isTokenValid(token: string): boolean {
    const payload = this.decodeToken(token);
    const expirationDate = payload?.exp * 1000; // exp is in seconds, convert to milliseconds
    return expirationDate > Date.now();
  }
  
  // this will be to check if the user is logged
  isLoggedIn() {
    const accessToken = this.getAccessToken();
    const refreshToken = this.getRefreshToken();
  
    // If tokens are found in local storage
    if (accessToken && refreshToken) {
      // Check if both tokens are valid
      return this.isTokenValid(accessToken) && this.isTokenValid(refreshToken);
    }
  
    return false; // Return false if no tokens are found or they are invalid
  }


  // LOGOUT LOGIC
  clearTokens(): void {
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
    this.router.navigate(['/login']); // Redirect to login
  }

  logout(): void {
    this.clearTokens();
    this.router.navigate(['login']);
  }

  // NAVIGATE TO RIGHT MODULE
  navigateToDashboard(): void {
    const userRole = this.getDecodedToken().role; // Assuming you have a method to decode the JWT
    console.log(userRole)

    if (userRole === 'admin') {
      this.router.navigate(['admin']);
    } else if (userRole === 'manager') {
      this.router.navigate(['manager']);
    } else if (userRole === 'employee') {
      this.router.navigate(['employee']);
    } else {
      console.error('Unknown user role:', userRole);
    }
  }

  // this are api calls

  login(email:String,password:string): Observable<any>{
    return this.http.post(`${this.baseUrl}authenticate`, {email, password})
  }

  verifyCode(code: string, email: string): Observable<any> {
    return this.http.post(`${this.baseUrl}verify`, { email, code });
  }

  getUserRole(): string | null {
    // Assuming the user role is stored in the token or localStorage after login
    return localStorage.getItem('user_role'); // Adjust this based on where you store the role
  }


}
