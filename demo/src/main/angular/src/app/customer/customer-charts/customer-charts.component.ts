import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import * as _ from 'lodash';
import { DynamicDialogConfig } from 'primeng/dynamicdialog';

@Component({
  selector: 'app-customer-charts',
  templateUrl: './customer-charts.component.html',
  styleUrls: ['./customer-charts.component.scss']
})
export class CustomerChartsComponent implements OnInit {
  data: any;

  constructor(
    private config: DynamicDialogConfig,
    private translateService: TranslateService
  ) {
    const result = _.countBy(config.data.customers, 'customerType');

    this.data = {
      labels: [
        this.translateService.instant('Vip'),
        this.translateService.instant('Regular'),
        this.translateService.instant('Standard'),
        this.translateService.instant('New')
      ],
      datasets: [
        {
          data: [result.A, result.B, result.C, result.D],
          backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#004E00'],
          hoverBackgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#008100']
        }
      ]
    };
  }

  ngOnInit(): void {}
}
