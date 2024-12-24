import {HttpClient, HttpEvent, HttpHeaders, HttpRequest} from '@angular/common/http';
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
  updateProjectBytitleForManager(data:any,title:String){
    return this.http.put(`${this.baseUrl}project/title/formanager/${title}`,data);
  }
  deleteProjectBytitle(title:String){
    return this.http.delete(`${this.baseUrl}project/title/${title}`);
  }

  getAllprojectsByManagerEmail(email:String):Observable<any>{
    return this.http.get(`${this.baseUrl}project/manager/${email}`);

  }

  upload(file: File | undefined, id: string): Observable<HttpEvent<any>> {
    if (!file) {
      throw new Error('No file selected');
    }

    const formData: FormData = new FormData();
    formData.append('file', file, file.name); // Append the file to the form data

    const url = `${this.baseUrl}project/upload/${id}`; // Format the URL with the ID
    const req = new HttpRequest('POST', url, formData, {
      headers: new HttpHeaders(),
      responseType: 'json',
    });

    return this.http.request(req);
  }


  getFilesByProjectId(id:number): Observable<any> {
    return this.http.get(`${this.baseUrl}project/filess/${id}`);
  }



}
