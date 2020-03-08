import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppHeaderModule } from '@coreui/angular';
import { TranslateModule } from '@ngx-translate/core';
import { PerfectScrollbarModule } from 'ngx-perfect-scrollbar';
import { NgxSpinnerModule } from 'ngx-spinner';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { DropdownModule } from 'primeng/dropdown';
import { ToastModule } from 'primeng/toast';
import { AuthxRoutingModule } from './authx-routing.module';
import { AuthxComponent } from './authx.component';
import { LoginComponent } from './login/login.component';

@NgModule({
  declarations: [AuthxComponent, LoginComponent],
  imports: [
    CommonModule,
    AuthxRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    // Coreui modules
    AppHeaderModule,
    PerfectScrollbarModule,
    ButtonModule,
    // PrimeNG modules
    DropdownModule,
    ToastModule,
    // Other modules
    TranslateModule,
    NgxSpinnerModule
  ],
  providers: [MessageService]
})
export class AuthxModule {}
