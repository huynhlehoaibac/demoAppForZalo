import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CallbackComponent } from './callback.component';
import { ZaloComponent } from './zalo/zalo.component';

const routes: Routes = [
  {
    path: '',
    component: CallbackComponent,
    children: [{ path: 'zalo', component: ZaloComponent }]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CallbackRoutingModule {}
