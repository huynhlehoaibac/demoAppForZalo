import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthxComponent } from './authx.component';
import { LoginComponent } from './login/login.component';

const routes: Routes = [
  {
    path: '',
    component: AuthxComponent,
    children: [
      {
        path: 'login',
        data: {
          title: 'Login'
        },
        component: LoginComponent
      },
      { path: '', pathMatch: 'full', redirectTo: 'login' }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthxRoutingModule {}
