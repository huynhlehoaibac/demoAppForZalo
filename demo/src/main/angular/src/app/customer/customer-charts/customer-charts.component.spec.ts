import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerChartsComponent } from './customer-charts.component';

describe('CustomerChartsComponent', () => {
  let component: CustomerChartsComponent;
  let fixture: ComponentFixture<CustomerChartsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CustomerChartsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomerChartsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
