import { HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { LangChangeEvent, TranslateService } from '@ngx-translate/core';
import {
  BreadcrumCreateButtonClickEvent,
  BreadcrumExportButtonClickEvent
} from '@shared/event';
import { CommonService, Notifier } from '@shared/service';
import { saveAs } from 'file-saver';
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
import { CreateCustomerComponent } from '../create-customer/create-customer.component';
import { UpdateCustomerComponent } from '../update-customer/update-customer.component';
import { CustomerChartsComponent } from '../customer-charts/customer-charts.component';

@Component({
  selector: 'app-customer-list',
  templateUrl: './customer-list.component.html',
  styleUrls: ['./customer-list.component.scss']
})
export class CustomerListComponent implements OnInit, OnDestroy {
  customers: any[];
  totalRecords: number;
  cols: any[];
  displayedColumns: any[];

  customerTypes: any[];
  statuses: SelectItem[];
  genders: SelectItem[];

  selectedCustomerType: any;
  selectedStatus: any;
  selectedGender: any;

  inputtedCustomerName: any;
  inputtedBalance: any;
  inputtedPhone: any;
  inputtedEmail: any;
  inputtedAddress: any;
  inputtedAccountNumber: any;

  private subscription = new Subscription();

  @ViewChild(Table)
  table: Table;

  defaultMultiSortMeta = [
    {
      field: 'customerName',
      order: 1
    },
    {
      field: 'email',
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
      )
      .add(
        this.notifier.on(BreadcrumExportButtonClickEvent).subscribe(event => {
          this.exportCustomers(event.fileType);
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

  private exportCustomers(fileType: string) {
    this.customerService
      .exportCustomers(
        this.table.createLazyLoadMetadata(),
        this.displayedColumns,
        fileType
      )
      .subscribe((result: HttpResponse<Blob>) => {
        let type: string;
        switch (fileType) {
          case 'pdf':
            type = 'application/pdf';
            break;
          case 'xlsx':
          default:
            type =
              'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
            break;
        }
        saveAs(
          new Blob([result.body], { type }),
          `customer-list-${Date.now()}.${fileType}`
        );
      });
  }

  private showCreateCustomerDialog() {
    this.dialogService
      .open(CreateCustomerComponent, {
        header: this.translate.instant('Create a new customer'),
        styleClass: 'w-lg-60 w-xl-40',
        contentStyle: {
          'max-height': 'calc(100vh - 105px - 50px - 30px)',
          'overflow-y': 'auto',
          'overflow-x': 'hidden'
        }
      })
      .onClose.subscribe(result => {
        if (result && result.hasChanges) {
          this.messageService.add({
            severity: 'success',
            summary: this.translate.instant('Create a new customer'),
            detail: this.translate.instant('Successfully added')
          });
          this.loadLazy(this.table.createLazyLoadMetadata());
        }
      });
  }

  onShowUpdateCustomerDialog(customer: any) {
    this.dialogService
      .open(UpdateCustomerComponent, {
        data: { customer },
        header: this.translate.instant('Update the customer'),
        styleClass: 'w-lg-60 w-xl-40',
        contentStyle: {
          'max-height': 'calc(100vh - 105px - 50px - 30px)',
          'overflow-y': 'auto',
          'overflow-x': 'hidden'
        }
      })
      .onClose.subscribe(result => {
        if (result && result.hasChanges) {
          this.messageService.add({
            severity: 'success',
            summary: this.translate.instant('Update the customer'),
            detail: this.translate.instant('Successfully saved')
          });
          this.loadLazy(this.table.createLazyLoadMetadata());
        }
      });
  }

  onDeleteCustomer(customer: any) {
    this.confirmationService.confirm({
      header: this.translate.instant('Delete the customer'),
      message: this.translate.instant(
        'Are you sure to delete the customer {{customerName}} (ID: {{customerId}})?',
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

  onShowCustomerChartDialog(customer: any) {
    this.dialogService.open(CustomerChartsComponent, {
      data: {
        customers: this.customers
      },
      header: this.translate.instant('Customer chart by type'),
      styleClass: 'w-lg-60 w-xl-40',
      contentStyle: {
        'max-height': 'calc(100vh - 105px - 50px - 30px)',
        'overflow-y': 'auto',
        'overflow-x': 'hidden'
      }
    });
  }

  onBalanceChange(event: any, dt: Table) {
    dt.filter(event.value, 'balance', 'gt');
  }

  onDisplayedColumnsChange(newValue) {
    this.displayedColumns = _.orderBy(newValue, ['index'], ['asc']);
  }

  private intTable() {
    this.cols = [
      {
        field: 'customerName',
        header: 'Name',
        width: '200px',
        sortable: true,
        index: 1
      },
      {
        field: 'customerType',
        header: 'Type',
        width: '120px',
        sortable: true,
        index: 2
      },
      {
        field: 'balance',
        header: 'Balance',
        width: '160px',
        sortable: true,
        index: 3
      },
      {
        field: 'phone',
        header: 'Phone',
        width: '140px',
        sortable: true,
        index: 4
      },
      {
        field: 'email',
        header: 'Email',
        width: '200px',
        sortable: true,
        index: 5
      },
      {
        field: 'address',
        header: 'Address',
        width: '200px',
        sortable: true,
        index: 6
      },
      {
        field: 'status',
        header: 'Status',
        width: '120px',
        sortable: true,
        index: 7
      },
      {
        field: 'accountNumber',
        header: 'Account No',
        width: '140px',
        sortable: true,
        index: 8
      },
      {
        field: 'gender',
        header: 'Gender',
        width: '100px',
        sortable: true,
        index: 9
      },
      {
        field: 'action',
        header: 'Action',
        width: '80px',
        rowspan: 2,
        sortableColumnDisabled: true,
        index: 10
      }
    ];

    this.displayedColumns = this.cols;
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

    const genders = this.customerService.getGenders();
    this.translate.get('All').subscribe(translatedText => {
      this.genders = [{ value: null, label: translatedText }];
      _.forEach(genders, gender => {
        this.genders.push({
          value: gender.value,
          label: this.translate.instant(gender.label)
        });
      });
    });
  }

  onResetTable() {
    this.inputtedCustomerName = null;
    this.selectedCustomerType = null;
    this.inputtedBalance = null;
    this.inputtedPhone = null;
    this.inputtedEmail = null;
    this.inputtedAddress = null;
    this.selectedStatus = null;
    this.inputtedAccountNumber = null;
    this.selectedGender = null;

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
