import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Check if the user is logged in
  const loggedIn = authService.isLoggedIn();
  const userRole = authService.getUserRole(); // Get the user role

  if (!loggedIn) {
    router.navigate(['login']);
    return false;
  }

  // Define allowed roles for each dashboard based on the route
  const expectedRole = route.data['role']; // Role defined in the route

  // Check if the user's role matches the expected role
  if (userRole !== expectedRole) {
    // Redirect to a 'not authorized' page or home page if roles don't match
    router.navigate(['not-authorized']);
    return false;
  }

  return true; // User is authenticated and has the correct role
};
