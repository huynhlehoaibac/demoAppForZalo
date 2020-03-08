import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(private jwtHelper: JwtHelperService, private http: HttpClient) {}

  isAuthenticated(accessToken: any): boolean {
    try {
      // Check whether the token is expired or not.
      return !this.jwtHelper.isTokenExpired(accessToken);
    } catch (e) {}

    return false;
  }

  saveToken(token: any) {
    localStorage.setItem('accessToken', token.accessToken);
    localStorage.setItem('refreshToken', token.refreshToken);
  }

  getAccessToken(): any {
    return localStorage.getItem('accessToken');
  }

  getAuthenticationUser(): any {
    const accessToken = this.getAccessToken();
    if (!accessToken) {
      return null;
    }

    return this.jwtHelper.decodeToken(accessToken);
  }

  removeAuthToken() {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
  }

  loginByCredentials(data: any): Observable<any> {
    return this.http.post(`${environment.authUrl}/login`, data);
  }

  loginBySSO(): Observable<any> {
    return this.http.post(`${environment.authUrl}/sso`, null);
  }

  exchangeToken(token): Observable<any> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.post(`${environment.authUrl}/exchange-token`, null, {
      headers,
      withCredentials: true
    });
  }

  refreshToken(): Observable<any> {
    const refreshToken = localStorage.getItem('refreshToken');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${refreshToken}`
    });

    return this.http.post(`${environment.authUrl}/token`, null, { headers });
  }

  sendResetPasswordEmail(data: any) {
    return this.http.patch(
      `${environment.authUrl}/send-password-reset-email`,
      data
    );
  }

  getUserType(emailAddress: any) {
    return this.http.get(
      `${environment.authUrl}/users/email-address/${emailAddress}/type`
    );
  }
}
