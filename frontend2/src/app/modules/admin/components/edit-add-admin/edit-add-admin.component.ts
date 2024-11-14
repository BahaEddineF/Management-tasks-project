import { ChangeDetectionStrategy, Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { UserService } from '../../../../services/users/user.service';



@Component({
  selector: 'app-edit-add-admin',
  templateUrl: './edit-add-admin.component.html',
  styleUrls: ['./edit-add-admin.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EditAddAdminComponent implements OnInit{
  createUserForm: FormGroup;
  updateUserForm: FormGroup;
  isEmployee: boolean = false;
  submitted: boolean = false; // Flag to check if the form has been submitted

  constructor(private _fb: FormBuilder,
              private dialogRef: MatDialogRef<EditAddAdminComponent>,
              private userService:UserService,
              @Inject(MAT_DIALOG_DATA) public data: any ) { // Inject MatDialogRef
    this.createUserForm = this._fb.group(
      {
        firstname: ['', Validators.required],
        lastname: ['', Validators.required],
        email: ['', [Validators.required, Validators.email]],
        phone: ['', Validators.required],
        role: ['', Validators.required],
        title: '', // Optional
        password: ['', [Validators.required, Validators.minLength(6)]],
        confirmPassword: ['', Validators.required],
      },
      { validators: this.passwordMatchValidator } // Add the custom validator here
    );

    // Subscribe to role value changes to toggle the employee-specific input
    this.createUserForm.get('role')?.valueChanges.subscribe(value => {
      this.isEmployee = value === 'EMPLOYEE';
    });

    this.updateUserForm = this._fb.group(
      {
        firstname: ['', Validators.required],
        lastname: ['', Validators.required],
        email: ['', [Validators.required, Validators.email]],
        phone: ['', Validators.required],
        role: ['', Validators.required],
        title: '', // Optional
      }
      );

    // Subscribe to role value changes to toggle the employee-specific input
    this.updateUserForm.get('role')?.valueChanges.subscribe(value => {
      this.isEmployee = value === 'EMPLOYEE';
    });
  }

  // Custom validator for password matching
  passwordMatchValidator(form: FormGroup) {
    const password = form.get('password')?.value;
    const confirmPassword = form.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { mismatch: true };
  }


  OnFormSubmit() {
    this.submitted = true; // Set the submitted flag to true

    // for the add user login
    if (!this.data){
      if (this.createUserForm.valid) {
        if(this.createUserForm.value.role=="ADMIN" || this.createUserForm.value.role=="MANAGER"){
          delete this.createUserForm.value.title
        }
        console.log(this.createUserForm.value)
        this.userService.addUser(this.createUserForm.value).subscribe({
          next: (val: any) => {
            alert('User added successfully');
            this.dialogRef.close(true)
          },
          error: (err:any) => {
            alert(err.error.message)
          }
        })
      } else {
        console.log('Form is invalid');
      }
    }
    else{
      // this for update user logic
      if (this.updateUserForm.valid) {
        if(this.updateUserForm.value.role != "EMPLOYEE"){

        this.userService.updateUser(this.updateUserForm.value,this.data.email)
        .subscribe({
          next: (res: any) => {
            alert("user updated successfully")
            this.dialogRef.close(true)
          },
          error: (err) => {
            alert(err.error.message)
          }
        })
      }
      else{
        this.userService.updateEmployee(this.updateUserForm.value,this.data.email)
        .subscribe({
          next: (res) => {
            alert("Employee updated successfully")
            this.dialogRef.close(true)
          },
          error: (err) => {
            alert(err.error.message)
          }
        })
      }
    }else{
      console.log('Form is invalid');

    }
  }
}

  ngOnInit(): void {
      this.updateUserForm.patchValue(this.data)
  }
}
