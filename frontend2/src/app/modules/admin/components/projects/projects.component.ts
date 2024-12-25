import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { ProjectService } from '../../../../services/projects/project.service';
import { MatDialog } from '@angular/material/dialog';
import { EditAddProjectComponent } from '../edit-add-project/edit-add-project.component';
import {Observable} from 'rxjs';

@Component({
  selector: 'app-projects',
  templateUrl: './projects.component.html',
  styleUrl: './projects.component.scss'
})
export class ProjectsComponent implements AfterViewInit{



  displayedColumns: string[] = ['title' , 'start_date' , 'end_date' , 'status' , 'files','manager', 'action'];
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
    this.projectService.getAllProjects().subscribe({
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

  openAddProjectForm(){
    const dialogRef = this._dialog.open(EditAddProjectComponent);
    dialogRef.afterClosed().subscribe({
      next:(val)=> {
          if(val){
            this.getProjectList()
          }
      },
    })
  }

  openEditForm(data :any){
    const dialogRef = this._dialog.open(EditAddProjectComponent,{data});
    dialogRef.afterClosed().subscribe({
      next:(val)=> {
          if(val){
            this.getProjectList()
          }
      },
    })
  }
  deleteProjectBytitle(title: String) {
    this.projectService.deleteProjectBytitle(title).subscribe({
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
