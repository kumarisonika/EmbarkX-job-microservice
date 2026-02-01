package com.embarkx.jobms.job.impl;


import com.embarkx.jobms.job.Job;
import com.embarkx.jobms.job.JobRepository;
import com.embarkx.jobms.job.JobService;
import com.embarkx.jobms.job.dto.JobWithCompanyDTO;
import com.embarkx.jobms.job.external.Company;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;

    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public List<JobWithCompanyDTO> findAll() {
        List<Job> jobs = jobRepository.findAll();
        List<JobWithCompanyDTO> jobWithCompanyDTOS = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();

        for (Job job: jobs){
            JobWithCompanyDTO jobWithCompanyDTO = new JobWithCompanyDTO();
            jobWithCompanyDTO.setJob(job);
            Company company = restTemplate.getForObject("http://localhost:8082/companies/"
                            +job.getCompanyId(), Company.class);
            jobWithCompanyDTO.setCompany(company);
            jobWithCompanyDTOS.add(jobWithCompanyDTO);
        }
        return jobWithCompanyDTOS;
    }

    @Override
    public void createJob(Job job) {
        jobRepository.save(job);

    }

    @Override
    public Job getJobById(Long id){
        return jobRepository.findById(id).orElseThrow(()-> new RuntimeException("Job not found!"));
    }

    @Override
    public boolean deleteJobById(Long id){
        if(!jobRepository.existsById(id)){return false;}
        jobRepository.deleteById(id);
        return true;
    }
}
