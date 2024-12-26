import { HttpClient, HttpEvent, HttpHeaders, HttpRequest } from '@angular/common/http';
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

    upload(file: File | undefined, id: string): Observable<HttpEvent<any>> {
      if (!file) {
        throw new Error('No file selected');
      }
  
      const formData: FormData = new FormData();
      formData.append('file', file, file.name); // Append the file to the form data
  
      const url = `${this.baseUrl}task/upload/${id}`; // Format the URL with the ID
      const req = new HttpRequest('POST', url, formData, {
        headers: new HttpHeaders(),
        responseType: 'json',
      });
  
      return this.http.request(req);
    }
  
  
    getFilesByProjectId(id:number): Observable<any> {
      return this.http.get(`${this.baseUrl}task/files/${id}`);
    }

}
