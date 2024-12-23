import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { EditAddAdminComponent } from '../edit-add-admin/edit-add-admin.component';
import { UserService } from '../../../../services/users/user.service';

import {MatTableDataSource, MatTableModule} from '@angular/material/table';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { Dialog } from '@angular/cdk/dialog';
import { MatSidenav } from '@angular/material/sidenav';
import { ImageUploadComponent } from '../image-upload/image-upload.component';



@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrl: './users.component.scss'
})
export class UsersComponent implements AfterViewInit {

  displayedColumns: string[] = ['email' , 'firstname' , 'lastname' , 'phone' , 'role' , 'action'];
  dataSource = new MatTableDataSource<any>;

  @ViewChild(MatPaginator) paginator !: MatPaginator;
  @ViewChild(MatSort) sort !: MatSort;

  constructor(private _dialog: MatDialog,private userservice: UserService,){}

  ngAfterViewInit(): void {
    this.getUsersList();
}

getUsersList(){
  this.userservice.getusersList().subscribe({
    next: (res) => {
      this.dataSource = new MatTableDataSource(res);
      this.dataSource.sort = this.sort
      this.dataSource.paginator = this.paginator
    },
    error: (err) =>{
      console.log(err)
    }
  })
}

  openAddEmpForm(){
    const dialogRef = this._dialog.open(EditAddAdminComponent);
    dialogRef.afterClosed().subscribe({
      next:(val)=> {
          if(val){
            this.getUsersList()
          }
      },
    })
  }

  openEditForm(data :any){
    const dialogRef = this._dialog.open(EditAddAdminComponent,{data});
    dialogRef.afterClosed().subscribe({
      next:(val)=> {
          if(val){
            this.getUsersList()
          }
      },
    })
  }




  deleteUserByEmail(email: string) {
    this.userservice.deleteUser(email).subscribe({
      next: (res) => {
        alert("User deleted successfully");
        window.location.reload(); // Reload the page after deletion
      },
      error: (err) => {
        alert(err);
      }
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }


  openImageUploadDialog(email: string) {
    const dialogRef = this._dialog.open(ImageUploadComponent, {
      data: { email }  // Pass the email to the dialog component
    });
  
    dialogRef.afterClosed().subscribe({
      next: (imageUrl) => {
        if (imageUrl) {
          console.log('Image uploaded successfully:', imageUrl);
          // You can handle the uploaded image URL here
        }
      }
    });
  }

}
