import { CommonModule } from '@angular/common';
import { NgModule, ModuleWithProviders } from '@angular/core';
import { RouterModule } from '@angular/router';

// Breadcrumb Component
import { BreadcrumbService } from './service/breadcrumb.service';
import { BreadcrumbComponent } from './breadcrumb.component';
import { TranslateModule, TranslateService } from '@ngx-translate/core';

// @dynamic
@NgModule({
  imports: [CommonModule, RouterModule, TranslateModule],
  exports: [BreadcrumbComponent],
  declarations: [BreadcrumbComponent]
})
export class BreadcrumbModule {
  static forRoot(config?: any): ModuleWithProviders<BreadcrumbModule> {
    return {
      ngModule: BreadcrumbModule,
      providers: [
        BreadcrumbService,
        TranslateService
      ]
    };
  }
}
