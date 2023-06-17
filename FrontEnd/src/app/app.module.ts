import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './components/header/header.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { FooterComponent } from './components/footer/footer.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { TablesDataComponent } from './components/tables-data/tables-data.component';
import { CustomersListComponent } from './components/customers-list/customers-list.component';
import {HttpClientModule}  from '@angular/common/http';
import { CustomerRegisterComponent } from './components/customer-register/customer-register.component';
import { FormsModule } from '@angular/forms';
import { CustomerUpdateComponent } from './components/customer-update/customer-update.component';
import { RouterModule } from '@angular/router';
import { CustomerDetailsComponent } from './components/customer-details/customer-details.component';
import { JobRegisterComponent } from './components/job-register/job-register.component';
import { JobsListComponent } from './components/jobs-list/jobs-list.component';
import { JobUpdateComponent } from './components/job-update/job-update.component';
import { CommonModule } from '@angular/common';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    SidebarComponent,
    FooterComponent,
    DashboardComponent,
    TablesDataComponent,
    CustomersListComponent,
    CustomerRegisterComponent,
    CustomerUpdateComponent,
    CustomerDetailsComponent,
    JobRegisterComponent,
    JobsListComponent,
    JobUpdateComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    RouterModule,
    CommonModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
