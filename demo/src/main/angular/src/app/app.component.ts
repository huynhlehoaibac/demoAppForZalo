import { Component, OnDestroy, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';
import { filter, map, mergeMap } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, OnDestroy {
  private subscription = new Subscription();

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private titleService: Title,
    private translate: TranslateService
  ) {}

  ngOnInit() {
    this.router.events.subscribe(evt => {
      if (!(evt instanceof NavigationEnd)) {
        return;
      }
      window.scrollTo(0, 0);
    });

    this.setDefaultLang();

    this.dynamicPageTitle();
  }

  private setDefaultLang() {
    this.translate.addLangs(['vi', 'en']);
    const lang = localStorage.getItem('lang') || this.getBrowserLang() || 'fr';

    localStorage.setItem('lang', lang);
    this.translate.currentLang = lang;
    this.translate.setDefaultLang(lang);
  }

  private getBrowserLang() {
    const browserLang = this.translate.getBrowserLang();
    const exists = this.translate.getLangs().includes(browserLang);
    if (exists) {
      return browserLang;
    }
    return null;
  }

  private dynamicPageTitle() {
    const subscription = this.router.events
      .pipe(
        filter(event => event instanceof NavigationEnd),
        map(() => this.activatedRoute),
        map(route => {
          while (route.firstChild) {
            route = route.firstChild;
          }
          return route;
        }),
        filter(route => route.outlet === 'primary'),
        mergeMap(route => route.data)
      )
      .subscribe(event => {
        if ('title' in event) {
          this.translate
            .get(event['title'])
            .subscribe(translatedTitle =>
              this.titleService.setTitle(translatedTitle)
            );
        }
      });
    this.subscription.add(subscription);
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
