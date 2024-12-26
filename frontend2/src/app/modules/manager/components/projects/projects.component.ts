import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { ProjectService } from '../../../../services/projects/project.service';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';
import { EditStatusComponent } from './edit-status/edit-status.component';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-projects',
  templateUrl: './projects.component.html',
  styleUrl: './projects.component.scss'
})
export class ProjectsComponent implements AfterViewInit{

  displayedColumns: string[] = ['title' , 'start_date' , 'end_date' , 'files' , 'status', 'action'];
  dataSource = new MatTableDataSource<any>;
    fileInfos?: Observable<any>;
    filesMap: { [projectId: number]: any[] } = {}; // Map project ID to files
    expandedProjectId: number | null = null;

  @ViewChild(MatPaginator) paginator !: MatPaginator;
  @ViewChild(MatSort) sort !: MatSort;

  constructor(private _dialog: MatDialog,private projectService: ProjectService){}

  ngAfterViewInit(): void {
    this.getProjectList();
  }




  getProjectList() {
    const email= localStorage.getItem("user_email");
    if (email != null){
    this.projectService.getAllprojectsByManagerEmail(email).subscribe({
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
}


applyFilter(event: Event) {
  const filterValue = (event.target as HTMLInputElement).value;
  this.dataSource.filter = filterValue.trim().toLowerCase();

  if (this.dataSource.paginator) {
    this.dataSource.paginator.firstPage();
  }
}

changeStatus(data :any) {
    const dialogRef = this._dialog.open(EditStatusComponent,{data});
    dialogRef.afterClosed().subscribe({
      next:(val)=> {
          if(val){
            this.getProjectList()
          }
      },
    })
  }

  toggleFiles(projectId: number) {
    if (this.expandedProjectId === projectId) {
      // Collapse if already expanded
      this.expandedProjectId = null;
    } else {
      // Expand and fetch files
      this.expandedProjectId = projectId;
      if (!this.filesMap[projectId]) {
        this.projectService.getFilesByProjectId(projectId).subscribe({
          next: (files) => {
            this.filesMap[projectId] = files;
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
