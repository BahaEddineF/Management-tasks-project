import { Component, computed, Input, signal } from '@angular/core';
import { AuthService } from '../../../../services/auth.service';
import { MenuItem } from '../../../admin/components/custom-sidenav/custom-sidenav.component';

@Component({
  selector: 'app-custom-sidenav2',
  templateUrl: './custom-sidenav.component.html',
  styleUrl: './custom-sidenav.component.scss'
})
export class CustomSidenavComponent {
  constructor(private authService:AuthService){}
  sideNavCollapsed = signal(false)
  @Input() set collapsed(val: boolean){
    this.sideNavCollapsed.set(val)
  }
  menuItems = signal<MenuItem[]>([
    {
      icon: "dashboard",
      label: "Dashboard",
      route: "home",
    },
    {
      icon: "work",
      label: "Projets",
      route: "projects",
    },
    {
      icon: "task",
      label: "Task",
      route: "tasks",
    },
    {
      icon: "logout",
      label: "Logout",
      route: "Logout",
    },])
  
  profilePicSize = computed(() => this.sideNavCollapsed() ? '32' : '100');


  logOut(): void{
    const confirmLogout = window.confirm('Are you sure you want to log out?');
    if (confirmLogout) {
    this.authService.logout();
  }

}

}
