import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProjectService } from '../../../../services/projects/project.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ManagerService } from '../../../../services/manager/manager.service';

@Component({
  selector: 'app-edit-add-project',
  templateUrl: './edit-add-project.component.html',
  styleUrl: './edit-add-project.component.scss'
})
export class EditAddProjectComponent implements OnInit {
  projectForm: FormGroup;
  managers: any[] = []; // Store fetched managers

  isEmployee: boolean = false;
  submitted: boolean = false; // Flag to check if the form has been submitted


  
  ngOnInit(): void {
    this.managerService.getAllManagers().subscribe({
      next: (res) =>{
        this.managers = res
      },
      error: (err) =>{
        console.log(err)
      }
    })
    this.projectForm.patchValue(this.data);
    
  }

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
              alert("project updates")
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








}
