package com.embarkx.jobms.job.impl;


import com.embarkx.jobms.job.Job;
import com.embarkx.jobms.job.JobRepository;
import com.embarkx.jobms.job.JobService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;

    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public List<Job> findAll() {
        return jobRepository.findAll();
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
