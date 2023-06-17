import { DatePipe } from '@angular/common';
import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Customer } from 'src/app/models/customer';
import { Job } from 'src/app/models/job';
import { Subjob } from 'src/app/models/subjob';
import { JobService } from 'src/app/services/job.service';

@Component({
  selector: 'app-job-update',
  templateUrl: './job-update.component.html',
  styleUrls: ['./job-update.component.css'],
  providers: [DatePipe]
})
export class JobUpdateComponent {

  customer: Customer = new Customer();
  job: Job = new Job();
  items: Subjob[] = [];

  constructor(
    private jobService: JobService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private datePipe: DatePipe) { }
    ngOnInit(){
      this.getJobById();
    }

    agregarItem() {
      this.job.subJobs.push({ subJobTitle: '', subJobAmount: 0 });
    }
    
    eliminarItem(index: number) {
      this.job.subJobs.splice(index, 1);
      this.calcularTotal();
    }

    calcularTotal(): number {
      let total = 0;
      if (Array.isArray(this.job.subJobs)) {
        for (let item of this.job.subJobs) {
          total += item.subJobAmount;
        }
      }
      this.job.totalAmount = total;
      return total;
    }

  borrarCero(item: any) {
    if (item.subJobAmount === 0) {
      item.subJobAmount = null;
    }
  }
  getJobById() {
    this.job.idJob = this.activatedRoute.snapshot.params['id'];
    this.jobService.getJobById(this.job.idJob).subscribe(
      jobFound => { 
        this.job = jobFound;
        const formattedDate = this.datePipe.transform(jobFound.date, 'yyyy-MM-dd');
        if (formattedDate) {
          this.job.date = formattedDate;
        }
        console.log(this.job);
      },
      error => { console.log(error) }
    );
  }

  updateJob() {
    const partesFecha = this.job.date.split('-');
    this.job.date = partesFecha[2] + '-' + partesFecha[1] + '-' + partesFecha[0]+' 00:00:00';
    this.jobService.updateJob(this.job).subscribe(
      response => { console.log("Se actualizó correctamente el trabajo al cliente", response) },
      error => { console.log(error) })
    this.router.navigate(['clientes']);
  }

}