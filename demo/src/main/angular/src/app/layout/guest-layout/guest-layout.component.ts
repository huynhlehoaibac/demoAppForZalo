import { Component, OnDestroy, OnInit } from '@angular/core';
import { LangChangeEvent, TranslateService } from '@ngx-translate/core';
import * as _ from 'lodash';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-guest-layout',
  templateUrl: './guest-layout.component.html',
  styleUrls: ['./guest-layout.component.scss']
})
export class GuestLayoutComponent implements OnInit, OnDestroy {
  langs: any[];
  currentLang: string;

  private subscription = new Subscription();

  constructor(private translate: TranslateService) {}

  ngOnInit() {
    this.langs = this.translate.langs.map(lang => {
      return { value: lang, label: lang.toUpperCase() };
    });
    this.currentLang = this.translate.currentLang;

    this.initListeners();
  }

  private initListeners() {
    this.subscription.add(
      this.translate.onLangChange.subscribe((event: LangChangeEvent) => {
        this.langChangeHandler(event.lang);
      })
    );
  }

  useLanguage(event: any) {
    this.translate.use(event.value);
  }

  private langChangeHandler(lang: string) {
    // save selected lang
    localStorage.setItem('lang', lang);
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
