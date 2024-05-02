import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  GuardResult,
  MaybeAsync,
  Router,
  RouterStateSnapshot,
} from '@angular/router';
import { AuthService } from '../services/authservice.service';
@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    if (this.authService.isAuthinticated()) {
      const requiredRoles = route.data['roles'];
      const userRole: string[] = [window.sessionStorage.getItem('role') || ''];
      // Allow the user to to proceed if no additional roles are required to access the route.
      if (!(requiredRoles instanceof Array) || requiredRoles.length === 0) {
        return true;
      }
      // Allow the user to proceed if all the required roles are present.
      if (requiredRoles.some((role) => userRole.includes(role))) {
        return true;
      } else {
        this.router.navigate(['/forbidden']);
        return false;
      }
    } else {
      this.router.navigate(['/forbidden']);
      return false;
    }
  }
}
