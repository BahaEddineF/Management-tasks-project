import { Component, Inject } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ManagerService } from '../../../../../services/manager/manager.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ProjectService } from '../../../../../services/projects/project.service';
import { EditAddProjectComponent } from '../../../../admin/components/edit-add-project/edit-add-project.component';

@Component({
  selector: 'app-edit-status',
  templateUrl: './edit-status.component.html',
  styleUrl: './edit-status.component.scss'
})
export class EditStatusComponent {
  projectForm: FormGroup;
  managers: any[] = []; // Store fetched managers
  submitted: boolean = false; // Flag to check if the form has been submitted
  currentManager: any;


  
  ngOnInit(): void {
    this.projectForm.patchValue(this.data);
  
  }

  constructor(private _fb: FormBuilder,
    private dialogRef: MatDialogRef<EditAddProjectComponent>,
    private projectService:ProjectService,
    @Inject(MAT_DIALOG_DATA) public data: any ){

      this.projectForm = this._fb.group(
        {
          title: ['', Validators.required],
          description: ['', Validators.required],
          start_date: ['', Validators.required],
          end_date: ['', Validators.required],
          status: ['', Validators.required],

        }
      );
    }

    OnFormSubmit() {

    if (this.projectForm.valid){
      this.projectForm.addControl('manager', new FormControl(this.data.manager)); // Adds newField with an empty value
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

