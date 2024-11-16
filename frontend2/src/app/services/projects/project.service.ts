import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  private baseUrl = 'http://localhost:4200/api/v1/';  // Replace with your backend API URL

  constructor(private http:HttpClient) { }

  getAllProjects():Observable<any> {
    return this.http.get(`${this.baseUrl}project/all`);
  }

  addProject(data:any):Observable<any>{
    return this.http.post(`${this.baseUrl}project/save`,data);
  }

  updateProjectBytitle(data:any,title:String){
    return this.http.put(`${this.baseUrl}project/title/${title}`,data);
  }

  deleteProjectBytitle(title:String){
    return this.http.delete(`${this.baseUrl}project/title/${title}`);
  }

  getAllprojectsByManagerEmail(email:String):Observable<any>{
    return this.http.get(`${this.baseUrl}project/manager/${email}`);

  }
  


}
