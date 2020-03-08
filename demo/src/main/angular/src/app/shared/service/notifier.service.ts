import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { filter } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class Notifier {
  private notifier$ = new Subject<any>();

  constructor() {}

  notify<T>(data: T): void {
    this.notifier$.next(data);
  }

  on<T>(type: { new (...args: any[]): T }): Observable<T> {
    return this.notifier$.pipe(filter(i => i instanceof type));
  }
}
