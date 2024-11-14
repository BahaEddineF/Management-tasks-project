import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl = 'http://localhost:4200/api/v1/';  // Replace with your backend API URL

  constructor(private http:HttpClient) { }

  addUser(data : any): Observable<any> {
    return this.http.post(`${this.baseUrl}auth/register`, data);
  }

  getusersList(): Observable<any> {
    return this.http.get(`${this.baseUrl}users`);

  }

  deleteUser(email: string): Observable<any>{
    return this.http.delete(`${this.baseUrl}users/email/${email}`);
  }

  updateUser(data: any,email:string): Observable<any>{
    return this.http.put(`${this.baseUrl}users/email/${email}`,data);
  }

  updateEmployee(data: any,email:string): Observable<any>{
    return this.http.put(`${this.baseUrl}employees/email/${email}`,data);
  }
}

