import { Component, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { TaskService } from '../../../../services/tasks/task.service';
import { MatDialog } from '@angular/material/dialog';
import { AddEditComponent } from './add-edit/add-edit.component';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-tasks',
  templateUrl: './tasks.component.html',
  styleUrl: './tasks.component.scss'
})
export class TasksComponent {
  displayedColumns: string[] = ['title' , 'start_date' , 'end_date' , 'status' , 'files' ,'project', 'employee', 'action'];
  dataSource = new MatTableDataSource<any>;
  fileInfos?: Observable<any>;
  filesMap: { [projectId: number]: any[] } = {}; // Map project ID to files
  expandedTask: number | null = null;  

  @ViewChild(MatPaginator) paginator !: MatPaginator;
  @ViewChild(MatSort) sort !: MatSort;

  constructor(private _dialog: MatDialog,private taskService: TaskService){}

  ngAfterViewInit(): void {
    this.getTaskList();
}
  getTaskList() {
      this.taskService.getAllTaskss().subscribe({
        next: (res) => {
          this.dataSource = new MatTableDataSource(res);
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;
          console.log(this.dataSource)
        },
        error: (err) =>{
          console.log(err)
        }
      })
  }

  openAddForm(){
    const dialogRef = this._dialog.open(AddEditComponent);
    dialogRef.afterClosed().subscribe({
      next:(val)=> {
          if(val){
            this.getTaskList()
          }
      },
    })
  }

  openEditForm(data :any){
    const dialogRef = this._dialog.open(AddEditComponent,{data});
    dialogRef.afterClosed().subscribe({
      next:(val)=> {
          if(val){
            this.getTaskList()
          }
      },
    })
  }
  deleteBytitle(title: String) {
    this.taskService.deleteTaskBytitle(title).subscribe({
      next: (res) => {
        alert("Project deleted successfully");
        window.location.reload(); // Reload the page after deletion
      },
      error: (err) => {
        console.log(err);
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


  toggleFiles(taskId: number) {
    if (this.expandedTask === taskId) {
      // Collapse if already expanded
      this.expandedTask = null;
    } else {
      // Expand and fetch files
      this.expandedTask = taskId;
      if (!this.filesMap[taskId]) {
        this.taskService.getFilesByProjectId(taskId).subscribe({
          next: (files) => {
            this.filesMap[taskId] = files;
            console.log(files);
          },
          error: (err) => {
            console.error(err);
          },
        });
      }
    }
  }
  


}
