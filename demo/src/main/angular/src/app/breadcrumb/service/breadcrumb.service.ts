import { Injectable } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import * as _ from 'lodash';
import { BehaviorSubject, Observable } from 'rxjs';
import { filter } from 'rxjs/operators';

@Injectable()
export class BreadcrumbService {
  breadcrumbs: Observable<Array<Object>>;
  private _breadcrumbs: BehaviorSubject<Array<Object>>;

  constructor(private router: Router, private route: ActivatedRoute) {
    this._breadcrumbs = new BehaviorSubject<Object[]>(new Array<Object>());
    this.breadcrumbs = this._breadcrumbs.asObservable();

    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(event => {
        const breadcrumbs = [];
        let currentRoute = this.route.root,
          url = '';
        do {
          const childrenRoutes = currentRoute.children;
          currentRoute = null;
          // tslint:disable-next-line:no-shadowed-variable
          _.forEach(childrenRoutes, route => {
            if (route.outlet === 'primary') {
              const routeSnapshot = route.snapshot;
              url +=
                '/' + routeSnapshot.url.map(segment => segment.path).join('/');
              breadcrumbs.push({
                label: route.snapshot.data,
                url: url
              });
              currentRoute = route;
            }
          });
        } while (currentRoute);

        this._breadcrumbs.next(Object.assign([], breadcrumbs));

        return breadcrumbs;
      });
  }
}
