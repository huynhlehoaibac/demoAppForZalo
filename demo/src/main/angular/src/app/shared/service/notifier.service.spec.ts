import { TestBed } from '@angular/core/testing';

import { Notifier } from './notifier.service';

describe('NotifierService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: Notifier = TestBed.get(Notifier);
    expect(service).toBeTruthy();
  });
});
