import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SelectItem } from 'primeng/api';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { FormGroup } from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class CommonService {
  constructor(private http: HttpClient) {}

  findMyAccount(): Observable<any> {
    return this.http.get(`${environment.apiUrl}/accounts/my-account`);
  }

  findAllOrganizations(): Observable<any> {
    return this.http.get(`${environment.apiUrl}/organizations`);
  }

  findAllApplications(): Observable<any> {
    return this.http.get(`${environment.apiUrl}/applications`);
  }

  findAllApplicationsExceptPPV2(): Observable<any> {
    return this.http.get(`${environment.apiUrl}/applications/except-ppv2`);
  }

  findAllApplicationsByUserId(): Observable<any> {
    return this.http.get(`${environment.apiUrl}/applications/by-user`);
  }

  findAllByExternalUserId(): Observable<any> {
    return this.http.get(`${environment.apiUrl}/applications/by-external-user`);
  }

  generateSelectItems(
    items: any[],
    label: string,
    value?: string
  ): SelectItem[] {
    const selectItems: SelectItem[] = [];
    if (items && items.length) {
      for (const item of items) {
        selectItems.push({
          label: item[label],
          value: value ? item[value] : item
        });
      }
    }
    return selectItems;
  }

  resetTable(table: any, defaultMultiSortMeta: any) {
    table._multiSortMeta = [...defaultMultiSortMeta];
    table.tableService.onSort(null);

    table.filteredValue = null;
    table.filters = {};

    table.first = 0;
    table.firstChange.emit(table.first);

    if (table.lazy) {
      const lazyLoadMetadata = table.createLazyLoadMetadata();
      lazyLoadMetadata.multiSortMeta = table._multiSortMeta;
      table.onLazyLoad.emit(lazyLoadMetadata);
    } else {
      table.totalRecords = table._value ? table._value.length : 0;
    }
  }

  handleSubmitError(error: any, form: FormGroup) {
    const errors = error.error.errors;
    for (const controlName of Object.keys(errors)) {
      const controlError = errors[controlName];
      if (controlName === '') {
        form.setErrors(controlError);
      } else {
        form.get(controlName).setErrors(controlError);
      }
    }
  }
}
