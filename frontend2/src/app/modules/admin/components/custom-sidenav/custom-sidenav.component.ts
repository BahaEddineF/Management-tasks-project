import { Component, computed, Input, signal } from '@angular/core';
import { AuthService } from '../../../../services/auth.service';

export type MenuItem = {
  icon: string;
  label: string;
  route: string;
};

@Component({
  selector: 'app-custom-sidenav',
  templateUrl: './custom-sidenav.component.html',
  styleUrls: ['./custom-sidenav.component.scss']
})
export class CustomSidenavComponent {
  profileImageUrl = signal<string | null>(null);
  headerTitle = signal<string>('Developer'); // Default value for title
  userName = signal<string>('Your Name'); // Default value for user name
  constructor(private authService: AuthService) {
    const userEmail = localStorage.getItem('user_email');

    if (userEmail) {
      this.profileImageUrl.set(`http://localhost:4200/api/v1/users/${userEmail}/profile-image`);
    }
    this.userName.set(userEmail || 'Your Name');
    this.headerTitle.set(localStorage.getItem('user_role') || 'DEVELOPER')

  }

  // Update the signal to default image when an error occurs
  onImageError() {
    this.profileImageUrl.set('/assets/img/pic.jpg');
  }

  sideNavCollapsed = signal(false);

  @Input() set collapsed(val: boolean) {
    this.sideNavCollapsed.set(val);
  }

  menuItems = signal<MenuItem[]>([
    {
      icon: 'dashboard',
      label: 'Dashboard',
      route: 'home',
    },
    {
      icon: 'supervisor_account',
      label: 'Users',
      route: 'users',
    },
    {
      icon: 'work',
      label: 'Projets',
      route: 'projects',
    },
    {
      icon: 'logout',
      label: 'Logout',
      route: 'Logout',
    },
  ]);

  profilePicSize = computed(() => (this.sideNavCollapsed() ? '32' : '100'));

  logOut(): void {
    const confirmLogout = window.confirm('Are you sure you want to log out?');
    if (confirmLogout) {
      this.authService.logout();
    }
  }
}
