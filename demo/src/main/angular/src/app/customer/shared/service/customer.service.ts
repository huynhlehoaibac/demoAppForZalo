import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import * as _ from 'lodash';
import { LazyLoadEvent, SelectItem } from 'primeng/api';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable()
export class CustomerService {
  constructor(private http: HttpClient) {}

  searchCustomers(event: LazyLoadEvent): Observable<any> {
    let params = new HttpParams();
    // Set page index, and size
    params = params.append('page', (event.first / event.rows) as any);
    params = params.append('size', event.rows as any);

    // Set filters data.
    _.forOwn(event.filters, (filterMetaData, col) => {
      params = params.append(col, filterMetaData.value);
    });

    _.forEach(event.multiSortMeta, ({ field, order }) => {
      params = params.append(
        'sort',
        `${_.snakeCase(field)},${order === 1 ? 'asc' : 'desc'}`
      );
    });
    params = params.append('sort', `${_.snakeCase('customerId')},asc`);

    return this.http.get(`${environment.apiUrl}/customers`, {
      params
    });
  }

  createCustomer(customer: any): Observable<any> {
    return this.http.post(
      `${environment.apiUrl}/customers/create-customer`,
      customer
    );
  }

  updateCustomer(customerId: number, customer: any): Observable<any> {
    return this.http.patch(
      `${environment.apiUrl}/customers/${customerId}/update-customer`,
      customer
    );
  }

  deleteCustomer(customerId: number): Observable<any> {
    return this.http.delete(`${environment.apiUrl}/customers/${customerId}`);
  }

  getCustomerTypes(): SelectItem[] {
    const items = [];
    items.push({ value: 1, label: 'Internal' });
    items.push({ value: 0, label: 'External' });
    return items;
  }

  getStatues(): SelectItem[] {
    const items = [];
    items.push({ value: true, label: 'Active' });
    items.push({ value: false, label: 'Blocked' });
    return items;
  }

  getCustomerRoles(): SelectItem[] {
    const items = [];
    items.push({ value: 'PARTNER', label: 'Partner' });
    items.push({ value: 'STANDARD_CUSTOMER', label: 'Standard customer' });
    items.push({ value: 'SERVICE_ADMIN', label: 'Service admin' });
    items.push({
      value: 'SERVICE_MANAGING_ADMIN',
      label: 'Service managing admin'
    });
    items.push({
      value: 'APPLICATION_MANAGING_ADMIN',
      label: 'Application managing admin'
    });
    items.push({ value: 'GENERAL_ADMIN', label: 'General admin' });
    return items;
  }
}
