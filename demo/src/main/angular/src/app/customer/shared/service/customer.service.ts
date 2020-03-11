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
        `${field},${order === 1 ? 'asc' : 'desc'}`
      );
    });
    params = params.append('sort', 'customerId,asc');

    return this.http.get(`${environment.apiUrl}/customers`, {
      params
    });
  }

  exportCustomers(
    event: LazyLoadEvent,
    displayedColumns: string[],
    fileType: string
  ) {
    const headers = new HttpHeaders({ Accept: 'application/octet-stream' });

    let params = new HttpParams();
    // Set filters data.
    _.forOwn(event.filters, (filterMetaData, field) => {
      params = params.append(field, filterMetaData.value);
    });

    _.forEach(event.multiSortMeta, ({ field, order }) => {
      params = params.append(
        'sort',
        `${field},${order === 1 ? 'asc' : 'desc'}`
      );
    });
    params = params.append('sort', 'customerId,asc');

    _.forEach(displayedColumns, ({ field }) => {
      params = params.append('displayedColumnFields', field);
    });

    params = params.append('fileType', fileType);

    return this.http.get(`${environment.apiUrl}/customers`, {
      headers,
      params,
      observe: 'response',
      responseType: 'blob'
    });
  }

  getCustomer(customerId: any): Observable<any> {
    return this.http.get(`${environment.apiUrl}/customers/${customerId}`);
  }

  createCustomer(customer: any): Observable<any> {
    return this.http.post(`${environment.apiUrl}/customers`, customer);
  }

  updateCustomer(customerId: number, customer: any): Observable<any> {
    return this.http.patch(
      `${environment.apiUrl}/customers/${customerId}`,
      customer
    );
  }

  deleteCustomer(customerId: number): Observable<any> {
    return this.http.delete(`${environment.apiUrl}/customers/${customerId}`);
  }

  getCustomerTypes(): SelectItem[] {
    const items = [];
    items.push({ value: 'A', label: 'Vip' });
    items.push({ value: 'B', label: 'Regular' });
    items.push({ value: 'C', label: 'Standard' });
    items.push({ value: 'D', label: 'New' });
    return items;
  }

  getStatues(): SelectItem[] {
    const items = [];
    items.push({ value: true, label: 'Active' });
    items.push({ value: false, label: 'Blocked' });
    return items;
  }

  getGenders(): SelectItem[] {
    const items = [];
    items.push({ value: 'M', label: 'Male' });
    items.push({ value: 'F', label: 'Female' });
    return items;
  }
}
