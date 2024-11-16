import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { ProjectService } from '../../../../services/projects/project.service';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-projects',
  templateUrl: './projects.component.html',
  styleUrl: './projects.component.scss'
})
export class ProjectsComponent implements AfterViewInit{

  displayedColumns: string[] = ['title' , 'start_date' , 'end_date' , 'status', 'action'];
  dataSource = new MatTableDataSource<any>;

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

changeStatus(row: any) {
  throw new Error('Method not implemented.');
  }
  

}
