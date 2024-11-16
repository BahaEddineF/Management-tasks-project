import { Component, Input } from '@angular/core';
import { faLock } from '@fortawesome/free-solid-svg-icons';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-verify-email',
  templateUrl: './verify-email.component.html',
  styleUrl: './verify-email.component.scss'
})
export class VerifyEmailComponent {

  @Input() email: string | null = null; // Use @Input to receive email

  faLock = faLock
  code: string = '';
  constructor(private authService: AuthService, private router: Router, private route: ActivatedRoute) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.email = params['email']; // Get email from query params
    });
  }

  verifyCode(): void {
    // Check if email is defined before calling verifyCode
    if (this.email) {
      this.authService.verifyCode(this.code, this.email).subscribe(
        (response: any) => {
          if (response.access_token) {
            this.authService.setToken(response.access_token,response.refresh_token);
            this.authService.setUserRole()
            this.authService.setUserEmail()
             // Store JWT token
            this.authService.navigateToDashboard(); // Redirect to the appropriate dashboard
          }
        },
        (error: any) => {
          console.error('Code verification failed', error);
        }
      );
    } else {
      console.error('Email is not defined.');
    }
  }



}
