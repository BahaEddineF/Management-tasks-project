import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { faLock } from '@fortawesome/free-solid-svg-icons';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']  // Corrected property name to styleUrls
})
export class LoginComponent implements OnInit {
  faLock = faLock;
  loginForm: FormGroup;

  constructor(private formBuilder: FormBuilder,private authService: AuthService, private router: Router) {
    // Initialize the form using FormBuilder
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],  // Added validators
      password: ['', [Validators.required]],  // Added validators
    });
  }

  ngOnInit(): void {
    if(this.authService.isLoggedIn()){
      this.authService.navigateToDashboard()
    }
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.authService.login(this.loginForm.value.email,this.loginForm.value.password).subscribe(
        (response: any) => {
          if (response.message) {
            this.router.navigate(['verify'], { queryParams: { email: this.loginForm.value.email } }); // Pass email in query params
          }
        },
        (error: any) => {
          alert("Username or Password Are Incorrect!")
        }
      );
    } 
}
}
