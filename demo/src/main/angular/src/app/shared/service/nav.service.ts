import { Injectable } from '@angular/core';
import { INavData } from '@coreui/angular';
import { TranslateService } from '@ngx-translate/core';
import * as _ from 'lodash';
import { Observable } from 'rxjs';
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
    const cloned = _.cloneDeep(navItems);
    return this.translate.get('All').pipe(
      map(() => {
        this.translateNavData(cloned);
        return cloned;
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
