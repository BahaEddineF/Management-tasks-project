import { Component, Inject } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { TaskService } from '../../../../../services/tasks/task.service';

@Component({
  selector: 'app-edit-status',
  templateUrl: './edit-status.component.html',
  styleUrl: './edit-status.component.scss'
})
export class EditStatusComponent {
  taskForm: FormGroup;
  submitted: boolean = false; // Flag to check if the form has been submitted
  project_title: string | undefined; // Store the title of the project
  constructor(private _fb: FormBuilder,
    private dialogRef: MatDialogRef<EditStatusComponent>,
    private taskService:TaskService,
    @Inject(MAT_DIALOG_DATA) public data: any ){

      this.taskForm = this._fb.group(
        {
          title: ['', Validators.required],
          description: ['', Validators.required],
          start_date: ['', Validators.required],
          end_date: ['', Validators.required],
          status: ['', Validators.required],
          project: [null, Validators.required]


        }
      );
    }

    ngOnInit(): void {
      // Patch the form with the values passed in the dialog
      this.taskForm.patchValue(this.data);

      // Set project title separately for display purposes
      this.project_title = this.data.project?.title;

      // If you want to populate the 'project' field with the whole project object:
    }


  OnFormSubmit() {

    if (this.taskForm.valid){
      this.taskForm.addControl('employee', new FormControl(this.data.employee));
      this.taskForm.addControl('project', new FormControl(this.data.project)); // Adds newField with an empty value
      this.taskService.updateTaskBytitleFormployee(this.taskForm.value,this.data.title).subscribe({
        next: (res) => {
          alert("task updates")
          this.dialogRef.close(true)
        },
        error: (err) =>{
          console.log(err.error.message)
        }
      })
      console.log(this.taskForm.value)
    }
  }

}
