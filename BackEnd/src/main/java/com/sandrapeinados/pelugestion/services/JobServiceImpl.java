package com.sandrapeinados.pelugestion.services;

import com.sandrapeinados.pelugestion.exceptions.ResourceNotFoundException;
import com.sandrapeinados.pelugestion.models.Customer;
import com.sandrapeinados.pelugestion.models.Job;
import com.sandrapeinados.pelugestion.models.SubJob;
import com.sandrapeinados.pelugestion.persistence.entities.CustomerEntity;
import com.sandrapeinados.pelugestion.persistence.entities.JobEntity;
import com.sandrapeinados.pelugestion.persistence.entities.SubJobEntity;
import com.sandrapeinados.pelugestion.persistence.repositories.IJobRepository;
import com.sandrapeinados.pelugestion.persistence.repositories.ISubJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl implements IJobService {

    @Autowired
    private IJobRepository jobRepo;
    @Autowired
    private ISubJobRepository subJobRepo;

    @Override
    public Job saveJob(Job job) {

        Customer customer = new Customer();
        customer.setId(job.getIdClient());
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(customer.getId());

        List<SubJob> list = job.getSubJobs();
        List<SubJobEntity> listSubJobsEntity = new ArrayList<>();
        double totalAmount = 0;
        //Convierte la lista de SubJob a SubJobEntity y va sumando los montos
        for (SubJob s : list) {
            SubJobEntity subJobEntity = new SubJobEntity();
            subJobEntity.setSubJobTitle(s.getSubJobTitle());
            subJobEntity.setSubJobAmount(s.getSubJobAmount());
            listSubJobsEntity.add(subJobEntity);
            totalAmount += subJobEntity.getSubJobAmount();
        }


        JobEntity jobToSave = new JobEntity();

        jobToSave.setJobTitle(job.getJobTitle());
        jobToSave.setJobDescription(job.getJobDescription());
        jobToSave.setTotalAmount(totalAmount);
        jobToSave.setDate(job.getDate());
        jobToSave.setCustomerEntity(customerEntity);
        //Se guarda primero el Job sin la lista de SubJobs porque necesita el Id del Job y JPA no lo está tomando
        jobRepo.save(jobToSave);

        //Agrega el Job a las SubJobs
        for (SubJobEntity s : listSubJobsEntity) {
            s.setJob(jobToSave);
        }
        //Se guarda la lista de SubJobs correctamente con el Id del Job que las vincula
        subJobRepo.saveAll(listSubJobsEntity);

        //Setea el Id de cada SubJob
        for (int i = 0; i < listSubJobsEntity.size(); i++) {
            list.get(i).setId(listSubJobsEntity.get(i).getId());
        }

        job.setIdJob(jobToSave.getJobId());
        job.setSubJobs(list);

        return job;
    }

    @Override
    public void deleteJob(Long id) {
        Job job = getJobById(id);
        jobRepo.deleteById(job.getIdJob());
    }


    @Override
    public Job getJobById(Long id) {
        Optional<JobEntity> jobFound = jobRepo.findById(id);

        if (jobFound.isPresent()) {
            Job job = new Job();
            List<SubJob> auxilirySubJobsList = new ArrayList<>();

            job.setIdClient(jobFound.get().getCustomerEntity().getId());
            job.setIdJob(jobFound.get().getJobId());
            job.setJobTitle(jobFound.get().getJobTitle());
            job.setJobDescription(jobFound.get().getJobDescription());
            job.setDate(jobFound.get().getDate());
            job.setTotalAmount(jobFound.get().getTotalAmount());

            List<SubJobEntity> subJobEntityList = jobFound.get().getSubJobs();
            for (SubJobEntity sub : subJobEntityList) {
                SubJob subJob = new SubJob();
                subJob.setId(sub.getId());
                subJob.setSubJobTitle(sub.getSubJobTitle());
                subJob.setSubJobAmount(sub.getSubJobAmount());
                auxilirySubJobsList.add(subJob);
            }
            job.setSubJobs(auxilirySubJobsList);
            return job;
        } else {
            throw new ResourceNotFoundException("Job Not Found");
        }
    }

    @Override
    public List<Job> getAllJobs() {
        List<JobEntity> jobEntityList = jobRepo.findAll();
        List<Job> jobsList = new ArrayList<>();

        for (JobEntity jobEntity : jobEntityList) {
            Job job = new Job();
            job.setIdClient(jobEntity.getCustomerEntity().getId());
            job.setIdJob(jobEntity.getJobId());
            job.setJobTitle(jobEntity.getJobTitle());
            job.setJobDescription(jobEntity.getJobDescription());
            job.setTotalAmount(jobEntity.getTotalAmount());
            job.setDate(jobEntity.getDate());

            List<SubJob> subJobsList = new ArrayList<>();
            List<SubJobEntity> subJobsEntity = jobEntity.getSubJobs();

            for (SubJobEntity subJobEntity : subJobsEntity) {
                SubJob subJob = new SubJob();
                subJob.setId(subJobEntity.getId());
                subJob.setSubJobTitle(subJobEntity.getSubJobTitle());
                subJob.setSubJobAmount(subJobEntity.getSubJobAmount());
                subJobsList.add(subJob);
            }
            job.setSubJobs(subJobsList);
            jobsList.add(job);
        }
        return jobsList;
    }

    @Transactional
    @Override
    public Job updateJob(Job job) {
        Optional<JobEntity> jobFound = jobRepo.findById(job.getIdJob());

        if (jobFound.isPresent()) {
            List<SubJobEntity> subJobsToUpdate = new ArrayList<>();

            for (SubJob s : job.getSubJobs()) {
                SubJobEntity subJobEntity = new SubJobEntity();
                subJobEntity.setId(s.getId());
                subJobEntity.setSubJobTitle(s.getSubJobTitle());
                subJobEntity.setSubJobAmount(s.getSubJobAmount());
                subJobEntity.setJob(jobFound.get());
                subJobsToUpdate.add(subJobEntity);
            }
            jobFound.get().setJobTitle(job.getJobTitle());
            jobFound.get().setJobDescription(job.getJobDescription());
            jobFound.get().setDate(job.getDate());
            jobFound.get().setTotalAmount(job.getTotalAmount());
            jobFound.get().getSubJobs().clear();
            jobFound.get().getSubJobs().addAll(subJobsToUpdate);
            jobRepo.save(jobFound.get());
            return job;
        } else {
            throw new ResourceNotFoundException("Job not found");
        }
    }
}
