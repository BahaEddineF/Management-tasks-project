import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ProjectService } from '../../../../../services/projects/project.service';
import { EmployeeService } from '../../../../../services/employees/employee.service';
import { TaskService } from '../../../../../services/tasks/task.service';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-add-edit',
  templateUrl: './add-edit.component.html',
  styleUrl: './add-edit.component.scss'
})
export class AddEditComponent  implements OnInit{
  tasktForm: FormGroup;
  employees: any[] = []; // Store fetched employees
  projects: any[] = []; // Store fetched projects
  submitted: boolean = false; // Flag to check if the form has been submitted
  currentEmployee: any;
  currentProject: any;

  currentFile?: File;
  message = '';
  fileInfos?: Observable<any>;
  


  constructor(private _fb: FormBuilder,
    private dialogRef: MatDialogRef<AddEditComponent>,
    private taskService: TaskService,
    private projectService:ProjectService,
    private employeeService:EmployeeService,
    @Inject(MAT_DIALOG_DATA) public data: any ){

      this.tasktForm = this._fb.group(
        {
          title: ['', Validators.required],
          description: ['', Validators.required],
          start_date: ['', Validators.required],
          end_date: ['', Validators.required],
          status: ['', Validators.required],
          employee: [null, Validators.required], // Optional
          project: [null, Validators.required] // Optional
        }
      );
    }


    ngOnInit(): void {
      this.employeeService.getAllEmployees().subscribe({
        next: (res) =>{
          this.employees = res
          if (this.data && this.data.employee ) {
             this.currentEmployee = this.employees.find(
              (employee) => employee.id === this.data.employee.id
            );
            if (this.currentEmployee) {
              this.currentEmployee.get('employee')?.setValue(this.currentEmployee.id);
            }
          }
          console.log(res)

        },
        error: (err) =>{
          console.log(err)
        }
      })

      this.projectService.getAllProjects().subscribe({
        next: (res) =>{
          this.projects = res
          if (this.data && this.data.project ) {
             this.currentProject = this.projects.find(
              (project) => project.id === this.data.project.id
            );
            if (this.currentProject) {
              this.currentProject.get('project')?.setValue(this.currentProject.id);
            }
          }
          console.log(res)
        },
        error: (err) =>{
          console.log(err)
        }
      })

      this.tasktForm.patchValue(this.data);
    
    }


    OnFormSubmit() {
      if (!this.data){
        if (this.tasktForm.valid){
          // console.log(this.projectForm.value);
          this.taskService.addTask(this.tasktForm.value).subscribe({
            next: (res) =>{
              alert("Task Added Successfully")
              this.dialogRef.close(true)

            },
            error : (err) => {
              alert(err.error.message)
            }
          })
        }
      }else{
        if (this.tasktForm.valid){
          this.taskService.updateTaskBytitle(this.tasktForm.value,this.data.title).subscribe({
            next: (res) => {
              alert("Task updated")
              this.dialogRef.close(true)
            },
            error: (err) =>{
              console.log(err.error.message)
            }
          })
          console.log(this.tasktForm.value)
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
          this.taskService.upload(this.currentFile, id).subscribe({
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
