import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { ChartModule } from 'primeng/chart';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { DropdownModule } from 'primeng/dropdown';
import { DialogService, DynamicDialogModule } from 'primeng/dynamicdialog';
import { InputTextModule } from 'primeng/inputtext';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { MultiSelectModule } from 'primeng/multiselect';
import { RadioButtonModule } from 'primeng/radiobutton';
import { SliderModule } from 'primeng/slider';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { CreateCustomerComponent } from './create-customer/create-customer.component';
import { CustomerChartsComponent } from './customer-charts/customer-charts.component';
import { CustomerListComponent } from './customer-list/customer-list.component';
import { CustomerRoutingModule } from './customer-routing.module';
import { CustomerComponent } from './customer.component';
import { CustomerService } from './shared/service';
import { UpdateCustomerComponent } from './update-customer/update-customer.component';

@NgModule({
  declarations: [
    CustomerComponent,
    CustomerListComponent,
    CreateCustomerComponent,
    UpdateCustomerComponent,
    CustomerChartsComponent
  ],
  imports: [
    CommonModule,
    CustomerRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    TranslateModule,
    // Primeng module.
    ConfirmDialogModule,
    DynamicDialogModule,
    InputTextModule,
    InputTextareaModule,
    RadioButtonModule,
    SliderModule,
    ButtonModule,
    DropdownModule,
    MultiSelectModule,
    TableModule,
    ToastModule,
    ChartModule
  ],
  providers: [
    ConfirmationService,
    DialogService,
    MessageService,
    CustomerService
  ]
})
export class CustomerModule {}
