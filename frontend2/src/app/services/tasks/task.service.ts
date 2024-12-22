import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TaskService {


  constructor(private http:HttpClient) { }
  private baseUrl = 'http://localhost:4200/api/v1/';  // Replace with your backend API URL


  getAllTaskss():Observable<any> {
    return this.http.get(`${this.baseUrl}task/all`);
  }

  deleteTaskBytitle(title: String) {
    return this.http.delete(`${this.baseUrl}task/title/${title}`);
  }



  updateTaskBytitle(data:any,title:String){
    return this.http.put(`${this.baseUrl}task/title/${title}`,data);
  }
  addTask(data:any):Observable<any>{
    return this.http.post(`${this.baseUrl}task/save`,data);
  }

  updateTaskBytitleFormployee(data:any,title:String){
    return this.http.put(`${this.baseUrl}task//title/foremployee/${title}`,data);
  }

  getAllTasksByEmployeeEmail(email:String):Observable<any>{
    return this.http.get(`${this.baseUrl}task/employee/${email}`);

  }

}
