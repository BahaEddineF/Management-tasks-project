import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ManagerService {
  private baseUrl = 'http://localhost:4200/api/v1/';  // Replace with your backend API URL

  constructor(private http:HttpClient) { }

  getAllManagers():Observable<any> {
    return this.http.get(`${this.baseUrl}manager/all`);
  }


}
