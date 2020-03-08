import { MediaMatcher } from '@angular/cdk/layout';
import { DOCUMENT } from '@angular/common';
import {
  ChangeDetectorRef,
  Component,
  Inject,
  OnDestroy,
  OnInit
} from '@angular/core';
import { Router } from '@angular/router';
import {
  BreadcrumCreateButtonClickEvent,
  BreadcrumExportButtonClickEvent
} from '@app/shared/event';
import { TokenRefreshEvent } from '@app/shared/event/token-refresh.event';
import { LangChangeEvent, TranslateService } from '@ngx-translate/core';
import { AuthService, NavService, Notifier } from '@shared/service';
import { ConfirmationService } from 'primeng/api';
import { DialogService } from 'primeng/dynamicdialog';
import { Subscription } from 'rxjs';
import { environment } from 'src/environments/environment';
import { COMPONENTS } from './component';

@Component({
  selector: 'app-authorized-layout',
  templateUrl: './authorized-layout.component.html',
  styleUrls: ['./authorized-layout.component.scss']
})
export class AuthorizedLayoutComponent implements OnInit, OnDestroy {
  components = COMPONENTS;

  navItems: any[];
  sidebarMinimized = true;
  private changes: MutationObserver;
  element: HTMLElement;

  mobileQuery: MediaQueryList;
  private mobileQueryEventListener: any;

  langs: any[];
  currentLang: string;
  authenticationUser: any;
  activatedComponent: any;
  private subscription = new Subscription();

  environment: any;

  constructor(
    private changeDetectorRef: ChangeDetectorRef,
    private media: MediaMatcher,
    private router: Router,
    private translate: TranslateService,
    private navService: NavService,
    private notifier: Notifier,
    private authService: AuthService,
    private dialogService: DialogService,
    private confirmationService: ConfirmationService,
    @Inject(DOCUMENT) _document?: any
  ) {
    this.changes = new MutationObserver(mutations => {
      this.sidebarMinimized = _document.body.classList.contains(
        'sidebar-minimized'
      );
    });
    this.element = _document.body;
    this.changes.observe(this.element as Element, {
      attributes: true,
      attributeFilter: ['class']
    });

    this.mobileQuery = this.media.matchMedia('(max-width: 40em)');
    this.mobileQueryEventListener = () =>
      this.changeDetectorRef.detectChanges();
    // this.mobileQuery.addEventListener('change', this.mobileQueryEventListener); // uncompatiable with IE11 :(
    // tslint:disable-next-line: deprecation
    this.mobileQuery.addListener(this.mobileQueryEventListener);
  }

  ngOnInit() {
    this.environment = environment;
    this.authenticationUser = this.authService.getAuthenticationUser();

    this.langs = this.translate.langs.map(lang => {
      return { value: lang, label: lang.toUpperCase() };
    });
    this.currentLang = this.translate.currentLang;

    this.translateSidebarNav();

    this.initListeners();
  }

  private initListeners() {
    this.subscription
      .add(
        this.translate.onLangChange.subscribe((event: LangChangeEvent) => {
          this.langChangeHandler(event.lang);
        })
      )
      .add(
        this.notifier.on(TokenRefreshEvent).subscribe(() => {
          this.authenticationUser = this.authService.getAuthenticationUser();
        })
      );
  }

  useLanguage(event: any) {
    this.translate.use(event.value);
  }

  private langChangeHandler(lang: string) {
    // save selected lang
    localStorage.setItem('lang', lang);

    this.translateSidebarNav();
  }

  private translateSidebarNav() {
    this.subscription.add(
      this.navService.ready().subscribe(navItems => {
        this.navItems = [...navItems];
      })
    );
  }

  onShowUpdateAccountDialog() {
    // this.dialogService
    //   .open(UpdateAccountComponent, {
    //     header: this.translate.instant('My account'),
    //     styleClass: 'w-lg-60 w-xl-40',
    //     contentStyle: {
    //       'max-height': 'calc(100vh - 105px - 50px - 30px)',
    //       'overflow-y': 'auto',
    //       'overflow-x': 'hidden'
    //     }
    //   })
    //   .onClose.subscribe(result => {
    //     if (result && result.hasChanges) {
    //       this.authService.refreshToken().subscribe(token => {
    //         this.authService.saveToken(token);
    //         this.notifier.notify(new TokenRefreshEvent());
    //       });
    //     }
    //   });
  }

  create() {
    this.notifier.notify(new BreadcrumCreateButtonClickEvent());
  }

  export() {
    this.notifier.notify(new BreadcrumExportButtonClickEvent());
  }

  onActive(componentRef: Component) {
    const component = this.components.find(
      i => componentRef instanceof i.component
    );
    this.activatedComponent = component ? component.name : undefined;
  }

  onLogout() {
    this.confirmationService.confirm({
      header: this.translate.instant('Logout'),
      message: this.translate.instant('Are you sure you want to logout?'),
      accept: () => {
        this.authService.removeAuthToken();
        this.router.navigate(['authx']);
      }
    });
  }

  ngOnDestroy(): void {
    // this.mobileQuery.removeEventListener('change', this.mobileQueryEventListener);
    // tslint:disable-next-line: deprecation
    this.mobileQuery.removeListener(this.mobileQueryEventListener);
    this.changes.disconnect();

    if (
      this.dialogService.dialogComponentRef &&
      this.dialogService.dialogComponentRef.instance
    ) {
      this.dialogService.dialogComponentRef.instance.close();
    }

    this.subscription.unsubscribe();
  }
}
