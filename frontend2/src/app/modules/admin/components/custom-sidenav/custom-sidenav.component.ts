import { Component, computed, Input, signal } from '@angular/core';
import { AuthService } from '../../../../services/auth.service';


export type MenuItem = {
  icon : string;
  label: string;
  route: string;
}

@Component({
  selector: 'app-custom-sidenav',
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
      icon: "supervisor_account",
      label: "Users",
      route: "users",
    },
    {
      icon: "work",
      label: "Projets",
      route: "projects",
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
