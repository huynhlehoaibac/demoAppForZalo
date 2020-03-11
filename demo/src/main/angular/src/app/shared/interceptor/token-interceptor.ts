import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { TokenRefreshEvent } from '@shared/event/token-refresh.event';
import { NgxSpinnerService } from 'ngx-spinner';
import { BehaviorSubject, EMPTY, Observable, throwError } from 'rxjs';
import { catchError, filter, finalize, switchMap, take } from 'rxjs/operators';
import { AuthService, Notifier } from '../service';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  isRefreshingToken = false;
  refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);
  counter = 0;

  constructor(
    private router: Router,
    private spinner: NgxSpinnerService,
    private notifier: Notifier,
    private authService: AuthService
  ) {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      catchError(error => {
        if (!request.url.includes('/api')) {
          return throwError(error);
        }

        if (error.status !== 401) {
          return throwError(error);
        }

        if (!this.isRefreshingToken) {
          this.isRefreshingToken = true;
          this.refreshTokenSubject.next(null);

          return this.authService.refreshToken().pipe(
            switchMap(token => {
              this.isRefreshingToken = false;
              this.refreshTokenSubject.next(token.accessToken);

              this.authService.saveToken(token);
              this.notifier.notify(new TokenRefreshEvent());

              return next.handle(this.addToken(request, token.accessToken));
            }),
            catchError(rtError => {
              this.authService.removeAuthToken();
              this.router.navigateByUrl('authx');

              return EMPTY;
            }),
            finalize(() => {
              this.isRefreshingToken = false;
            })
          );
        } else {
          return this.refreshTokenSubject.pipe(
            filter(accessToken => accessToken !== null),
            take(1),
            switchMap(accessToken =>
              next.handle(this.addToken(request, accessToken))
            )
          );
        }
      })
    );
  }

  private addToken(
    req: HttpRequest<any>,
    accessToken: string
  ): HttpRequest<any> {
    return req.clone({
      setHeaders: { Authorization: `Bearer ${accessToken}` }
    });
  }
}
