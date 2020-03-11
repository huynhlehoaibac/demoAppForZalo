import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

@Injectable()
export class LoaderInterceptor implements HttpInterceptor {
  counter = 0;

  constructor(private spinner: NgxSpinnerService) {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    Promise.resolve(null).then(() => this.showLoader());

    return next.handle(request).pipe(finalize(() => this.hideLoader()));
  }

  private showLoader(): void {
    if (this.counter === 0) {
      this.spinner.show();
    }

    this.counter++;
  }

  private hideLoader(): void {
    this.counter--;

    if (this.counter === 0) {
      this.spinner.hide();
    }
  }
}
