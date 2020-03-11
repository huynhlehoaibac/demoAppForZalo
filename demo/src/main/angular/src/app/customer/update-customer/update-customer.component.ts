import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import * as _ from 'lodash';
import { SelectItem } from 'primeng/api/selectitem';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import { CustomerService } from '../shared/service';

@Component({
  selector: 'app-update-customer',
  templateUrl: './update-customer.component.html',
  styleUrls: ['./update-customer.component.scss']
})
export class UpdateCustomerComponent implements OnInit {
  form: FormGroup;

  customerTypes: SelectItem[];
  statuses: SelectItem[];
  genders: SelectItem[];

  constructor(
    private fb: FormBuilder,
    private translate: TranslateService,
    private customerService: CustomerService,
    private ref: DynamicDialogRef,
    private config: DynamicDialogConfig
  ) {}

  ngOnInit(): void {
    this.initForm();

    this.initDropdown();
  }

  onSubmit() {
    const customerId = this.form.get('customerId').value;
    const formData = this.form.value;
    this.customerService.updateCustomer(customerId, formData).subscribe(() => {
      this.ref.close({
        hasChanges: true
      });
    });
  }

  onClose() {
    this.ref.close();
  }

  private initForm() {
    this.form = this.fb.group({
      customerId: [{ value: null, disabled: true }],
      customerName: [null, [Validators.required, Validators.maxLength(256)]],
      customerType: [null, [Validators.required, Validators.maxLength(1)]],
      balance: [null, [Validators.pattern(/^\d{0,14}(\.\d{1,2})?$/)]],
      phone: [null, [Validators.required, Validators.maxLength(16)]],
      email: [null, [Validators.maxLength(256)]],
      address: [null, [Validators.maxLength(256)]],
      status: [null],
      accountNumber: [null, [Validators.maxLength(256)]],
      gender: [null, [Validators.maxLength(1)]]
    });

    const customerId = this.config.data.customer.customerId;
    this.customerService.getCustomer(customerId).subscribe(
      customer => {
        this.form.patchValue(customer);
      },
      error => {}
    );
  }

  private initDropdown() {
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
}
