import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProjectService } from '../../../../services/projects/project.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ManagerService } from '../../../../services/manager/manager.service';
import {Observable} from 'rxjs';
import {HttpResponse} from '@angular/common/http';

@Component({
  selector: 'app-edit-add-project',
  templateUrl: './edit-add-project.component.html',
  styleUrl: './edit-add-project.component.scss'
})
export class EditAddProjectComponent implements OnInit {
  projectForm: FormGroup;
  managers: any[] = []; // Store fetched managers
  submitted: boolean = false; // Flag to check if the form has been submitted
  currentManager: any;
  currentFile?: File;
  message = '';
  fileInfos?: Observable<any>;


  constructor(private _fb: FormBuilder,
    private dialogRef: MatDialogRef<EditAddProjectComponent>,
    private projectService:ProjectService,
    private managerService:ManagerService,
    @Inject(MAT_DIALOG_DATA) public data: any ){

      this.projectForm = this._fb.group(
        {
          title: ['', Validators.required],
          description: ['', Validators.required],
          start_date: ['', Validators.required],
          end_date: ['', Validators.required],
          status: ['', Validators.required],
          manager: [null, Validators.required] // Optional

        }
      );
    }


  ngOnInit(): void {
    this.managerService.getAllManagers().subscribe({
      next: (res) =>{
        this.managers = res
        if (this.data && this.data.manager) {
           this.currentManager = this.managers.find(
            (manager) => manager.id === this.data.manager.id
          );
          if (this.currentManager) {
            this.projectForm.get('manager')?.setValue(this.currentManager.id);
          }
        }
      },
      error: (err) =>{
        console.log(err)
      }
    })
    this.projectForm.patchValue(this.data);

  }


  OnFormSubmit() {
    if (!this.data){
      if (this.projectForm.valid){
        // console.log(this.projectForm.value);
        this.projectService.addProject(this.projectForm.value).subscribe({
          next: (res) =>{
            alert("project Added Successfully")
            this.dialogRef.close(true)

          },
          error : (err) => {
            alert(err.error.message)
          }
        })
      }
    }else{
      if (this.projectForm.valid){
        this.projectService.updateProjectBytitle(this.projectForm.value,this.data.title).subscribe({
          next: (res) => {
            alert("project updated successfully")
            this.dialogRef.close(true)
          },
          error: (err) =>{
            console.log(err.error.message)
          }
        })
        console.log(this.projectForm.value)
      }
    }
  }


  selectFile(event: any): void {
    this.message = '';
    this.currentFile = event.target.files.item(0);
  }

  upload(): void {
    if (this.currentFile) {
      // Assuming `this.data.id` contains the ID you're trying to use in the URL
      const id = this.data.id; // Ensure that this is the correct property

      // Make sure `this.projectService.upload()` is using the correct URL format
      this.projectService.upload(this.currentFile, id).subscribe({
        next: (event: any) => {
          if (event instanceof HttpResponse) {
            this.message = event.body.message || 'File uploaded successfully!';
            console.log(this.message);
          }
        },
        error: (err: any) => {
          console.log(err);
          this.message = err.error?.message || 'Could not upload the file!';
        },
        complete: () => {
          this.currentFile = undefined;
        },
      });
    }
  }


}
