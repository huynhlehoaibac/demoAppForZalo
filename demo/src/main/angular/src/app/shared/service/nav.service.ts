import { Injectable } from '@angular/core';
import { INavData } from '@coreui/angular';
import { TranslateService } from '@ngx-translate/core';
import * as _ from 'lodash';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { AuthService } from './auth.service';
import { navItems } from './_nav';

@Injectable({
  providedIn: 'root'
})
export class NavService {
  constructor(
    private translate: TranslateService,
    private authService: AuthService
  ) {}

  ready(): Observable<Array<INavData>> {
    const authenticationUser = this.authService.getAuthenticationUser();
    const highestUserRole = authenticationUser.highestUserRole;

    const clone = _.cloneDeep(navItems);

    let items = [];
    switch (highestUserRole) {
      case 'GENERAL_ADMIN':
      case 'APPLICATION_MANAGING_ADMIN':
        // services, users, messages
        items = clone.filter(navItem =>
          /^(services|users|messages)$/i.test(navItem.name)
        );
        break;
      case 'SERVICE_MANAGING_ADMIN':
      case 'SERVICE_ADMIN':
        // services, users
        items = clone.filter(navItem =>
          /^(services|users)$/i.test(navItem.name)
        );
        break;
      case 'PARTNER':
        // services
        items = clone.filter(navItem => /^(services)$/i.test(navItem.name));
        break;
      default:
        break;
    }

    // possible to be a http request here
    return this.translate.get('All').pipe(
      map(() => {
        this.translateNavData(items);
        return items;
      })
    );
  }

  private translateNavData(items: INavData[]): void {
    for (const item of items) {
      if ('name' in item) {
        item.name = this.translate.instant(item.name);
      }
      if (item.children && item.children.length > 0) {
        this.translateNavData(item.children);
      }
    }
  }
}
