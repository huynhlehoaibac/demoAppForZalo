import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { NgxSpinnerModule } from 'ngx-spinner';
import { CallbackRoutingModule } from './callback-routing.module';
import { CallbackComponent } from './callback.component';
import { ZaloComponent } from './zalo/zalo.component';

@NgModule({
  declarations: [CallbackComponent, ZaloComponent],
  imports: [
    CommonModule,
    CallbackRoutingModule,
    // Others module
    NgxSpinnerModule
  ]
})
export class CallbackModule {}
