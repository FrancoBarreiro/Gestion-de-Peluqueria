import { Component, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { CustomersListComponent } from './components/customers-list/customers-list.component';
import { CustomerRegisterComponent } from './components/customer-register/customer-register.component';
import { CustomerUpdateComponent } from './components/customer-update/customer-update.component';

//Agregar las rutas de los componentes
const routes: Routes = [
  {path: '', component: DashboardComponent},
  {path: 'clientes', component: CustomersListComponent },
  {path: 'clientes/registrar', component: CustomerRegisterComponent},
  {path: 'clientes/actualizar/:id', component:CustomerUpdateComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }