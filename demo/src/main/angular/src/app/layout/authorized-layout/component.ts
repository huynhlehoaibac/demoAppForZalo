import { CustomerComponent } from '@app/customer';

export const COMPONENTS: { name: string; component: any; buttons?: any }[] = [
  {
    name: 'CustomerComponent',
    component: CustomerComponent,
    buttons: [
      {
        create: [
          'GENERAL_ADMIN',
          'APPLICATION_MANAGING_ADMIN',
          'SERVICE_MANAGING_ADMIN'
        ]
      },
      {
        export: [
          'GENERAL_ADMIN',
          'APPLICATION_MANAGING_ADMIN',
          'SERVICE_MANAGING_ADMIN'
        ]
      }
    ]
  }
];
