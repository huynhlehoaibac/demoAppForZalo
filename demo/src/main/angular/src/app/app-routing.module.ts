import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuardService } from '@shared/service';
import { AuthorizedLayoutComponent } from './layout/authorized-layout/authorized-layout.component';
import { GuestLayoutComponent } from './layout/guest-layout/guest-layout.component';

const routes: Routes = [
  {
    path: '',
    component: AuthorizedLayoutComponent,
    children: [
      {
        path: 'customers',
        loadChildren: () =>
          import('./customer/customer.module').then(m => m.CustomerModule)
      },
      {
        path: '',
        redirectTo: 'customers',
        pathMatch: 'full'
      }
    ],
    canActivate: [AuthGuardService]
  },
  {
    path: 'authx',
    component: GuestLayoutComponent,
    children: [
      {
        path: '',
        loadChildren: () =>
          import('./authx/authx.module').then(m => m.AuthxModule)
      }
    ]
  },
  {
    path: 'callback',
    children: [
      {
        path: '',
        loadChildren: () =>
          import('./callback/callback.module').then(m => m.CallbackModule)
      }
    ]
  },
  {
    path: 'page-not-found',
    loadChildren: () =>
      import('./page-not-found/page-not-found.module').then(
        m => m.PageNotFoundModule
      )
  },
  {
    path: '**',
    redirectTo: 'page-not-found'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
