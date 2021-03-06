import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean {
    const accessToken = this.authService.getAccessToken();
    if (!this.authService.isAuthenticated(accessToken)) {
      this.router.navigate(['authx']);
      return false;
    }

    return true;
  }
}
