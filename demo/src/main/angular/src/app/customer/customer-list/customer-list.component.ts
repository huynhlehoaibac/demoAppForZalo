import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { LangChangeEvent, TranslateService } from '@ngx-translate/core';
import { BreadcrumCreateButtonClickEvent } from '@shared/event';
import { CommonService, Notifier } from '@shared/service';
import * as _ from 'lodash';
import {
  ConfirmationService,
  LazyLoadEvent,
  MessageService,
  SelectItem
} from 'primeng/api';
import { DialogService } from 'primeng/dynamicdialog';
import { Table } from 'primeng/table';
import { Subscription } from 'rxjs';
import { CustomerService } from '../shared/service';

@Component({
  selector: 'app-customer-list',
  templateUrl: './customer-list.component.html',
  styleUrls: ['./customer-list.component.scss']
})
export class CustomerListComponent implements OnInit, OnDestroy {
  authenticationCustomer: any;
  customers: any[];
  totalRecords: number;
  cols: any[];
  customerTypes: any[];
  statuses: SelectItem[];
  customerRoles: SelectItem[];
  selectedCustomerType: any;
  selectedStatus: any;
  selectedCustomerRole: any;
  inputtedLastname: any;
  inputtedFirstname: any;
  inputtedCompanyName: any;
  inputtedEmailAddress: any;
  private subscription = new Subscription();

  @ViewChild(Table)
  table: Table;

  defaultMultiSortMeta = [
    {
      field: 'name',
      order: 1
    }
  ];

  constructor(
    private notifier: Notifier,
    private commonService: CommonService,
    private translate: TranslateService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private dialogService: DialogService,
    private customerService: CustomerService
  ) {}

  ngOnInit() {
    this.intTable();
    this.loadFiltersOptions();
    this.initListeners();
  }

  private initListeners() {
    this.subscription
      .add(
        this.translate.onLangChange.subscribe((event: LangChangeEvent) => {
          this.loadFiltersOptions();
        })
      )
      .add(
        this.notifier.on(BreadcrumCreateButtonClickEvent).subscribe(event => {
          this.showCreateCustomerDialog();
        })
      );
  }

  loadLazy(event: LazyLoadEvent) {
    this.customerService.searchCustomers(event).subscribe(
      result => {
        this.customers = result.content;
        this.totalRecords = result.totalElements;
      },
      error => {}
    );
  }

  private showCreateCustomerDialog() {
    // this.dialogService
    //   .open(CreateCustomerComponent, {
    //     header: this.translate.instant('Create a new customer'),
    //     styleClass: 'w-lg-60 w-xl-40',
    //     contentStyle: {
    //       'max-height': 'calc(100vh - 105px - 50px - 30px)',
    //       'overflow-y': 'auto',
    //       'overflow-x': 'hidden'
    //     }
    //   })
    //   .onClose.subscribe(result => {
    //     if (result && result.hasChanges) {
    //       this.messageService.add({
    //         severity: 'success',
    //         summary: this.translate.instant('Create a new customer'),
    //         detail: this.translate.instant('Successfully added')
    //       });
    //       this.loadLazy(this.table.createLazyLoadMetadata());
    //     }
    //   });
  }

  onShowUpdateCustomerDialog(customer: any) {
    // this.dialogService
    //   .open(UpdateCustomerComponent, {
    //     data: {
    //       customer: customer
    //     },
    //     header: this.translate.instant('Update the customer'),
    //     styleClass: 'w-lg-60 w-xl-40',
    //     contentStyle: {
    //       'max-height': 'calc(100vh - 105px - 50px - 30px)',
    //       'overflow-y': 'auto',
    //       'overflow-x': 'hidden'
    //     }
    //   })
    //   .onClose.subscribe(result => {
    //     if (result && result.hasChanges) {
    //       if (result.argosCustomer && result.argosCustomer.active !== '1') {
    //         this.confirmationService.confirm({
    //           key: 'alert',
    //           header: this.translate.instant('Update from Argos'),
    //           message: this.translate.instant(
    //             // tslint:disable-next-line: max-line-length
    //             'The customer is no longer a Bouygues Construction employee, if you want the customer to be permanently deleted from the application, please make this request via an Oasis incident.'
    //           )
    //         });
    //       }
    //       this.messageService.add({
    //         severity: 'success',
    //         summary: this.translate.instant('Update the customer'),
    //         detail: this.translate.instant('Successfully saved')
    //       });
    //       this.loadLazy(this.table.createLazyLoadMetadata());
    //     }
    //   });
  }

  onDeleteCustomer(customer: any) {
    this.confirmationService.confirm({
      header: this.translate.instant('Delete the customer'),
      message: this.translate.instant(
        'Are you sure to delete the customer {{lastname}} {{firstname}} ({{emailAddress}})?',
        customer
      ),
      accept: () => {
        this.customerService.deleteCustomer(customer.customerId).subscribe(
          () => {
            this.messageService.add({
              severity: 'success',
              summary: this.translate.instant('Delete the customer'),
              detail: this.translate.instant('Successfully deleted')
            });
            this.loadLazy(this.table.createLazyLoadMetadata());
          },
          error => {
            this.messageService.add({
              severity: 'error',
              summary: this.translate.instant('Delete the customer'),
              detail: error.error.message
            });
          }
        );
      }
    });
  }

  private intTable() {
    let defaultCols = [
      { field: 'filter', header: null, width: '50px', sortable: true },
      { field: 'customerName', header: 'Customer Name', width: '15%', sortable: true },
      { field: 'customerType', header: 'Customer Type', width: '15%', sortable: true },
      { field: 'balance', header: 'Balance', width: '15%', sortable: true },
      { field: 'phone', header: 'Phone', width: '30%', sortable: true },
      { field: 'email', header: 'Email', width: '100px', sortable: true },
      { field: 'address', header: 'Address', width: '100px', sortable: true },
      { field: 'status', header: 'status', width: '100px', sortable: true },
      { field: 'accountNumber', header: 'accountNumber', width: '100px', sortable: true },
      { field: 'gender', header: 'gender', width: '100px', sortable: true },
      {
        field: 'action',
        header: 'Action',
        width: '140px',
        rowspan: 2,
        sortableColumnDisabled: true
      }
    ];

    this.cols = defaultCols;
  }

  private loadFiltersOptions() {
    const customerTypes = this.customerService.getCustomerTypes();
    this.translate.get('All').subscribe(translatedText => {
      this.customerTypes = [{ value: null, label: translatedText }];
      _.forEach(customerTypes, status => {
        this.customerTypes.push({
          value: status.value,
          label: this.translate.instant(status.label)
        });
      });
    });

    const statuses = this.customerService.getStatues();
    this.translate.get('All').subscribe(translatedText => {
      this.statuses = [{ value: null, label: translatedText }];
      _.forEach(statuses, status => {
        this.statuses.push({
          value: status.value,
          label: this.translate.instant(status.label)
        });
      });
    });

    const customerRoles = this.customerService.getCustomerRoles();
    this.translate.get('All').subscribe(translatedText => {
      this.customerRoles = [{ value: null, label: translatedText }];
      _.forEach(customerRoles, status => {
        this.customerRoles.push({
          value: status.value,
          label: this.translate.instant(status.label)
        });
      });
    });
  }

  onResetTable() {
    this.inputtedLastname = null;
    this.inputtedFirstname = null;
    this.inputtedCompanyName = null;
    this.inputtedEmailAddress = null;
    this.selectedCustomerType = null;
    this.selectedStatus = null;
    this.selectedCustomerRole = null;

    this.commonService.resetTable(this.table, this.defaultMultiSortMeta);
  }

  ngOnDestroy() {
    if (
      this.dialogService.dialogComponentRef &&
      this.dialogService.dialogComponentRef.instance
    ) {
      this.dialogService.dialogComponentRef.instance.close();
    }

    this.subscription.unsubscribe();
  }
}
