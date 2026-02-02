package com.embarkx.jobms.job.impl;


import com.embarkx.jobms.job.Job;
import com.embarkx.jobms.job.JobRepository;
import com.embarkx.jobms.job.JobService;
import com.embarkx.jobms.job.dto.JobDTO;
import com.embarkx.jobms.job.external.Company;
import com.embarkx.jobms.job.external.Review;
import com.embarkx.jobms.job.mapper.JobMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.List;

@Service
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;
    @Autowired
    RestTemplate restTemplate;

    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public List<JobDTO> findAll() {
        List<Job> jobs = jobRepository.findAll();
        List<JobDTO> jobDTOS = new ArrayList<>();
//        RestTemplate restTemplate = new RestTemplate();

        for (Job job: jobs){
            jobDTOS.add(convertToDto(job));
        }
        return jobDTOS;
    }

    private JobDTO convertToDto(Job job){
        Company company = restTemplate.getForObject("http://COMPANYMS:8082/companies/"
                +job.getCompanyId(), Company.class);
        ResponseEntity<List<Review>> reviewResponse = restTemplate.exchange("http://REVIEWMS:8083/reviews?companyId="
                + job.getCompanyId(), HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Review>>() {
        });

        List<Review> reviews = reviewResponse.getBody();

        JobDTO jobDTO = JobMapper.mapToJobWithCompanyDto(job,company,reviews);
        jobDTO.setCompany(company);
        return jobDTO;
    }

    @Override
    public void createJob(Job job) {
        jobRepository.save(job);

    }

    @Override
    public JobDTO getJobById(Long id){
        Job job = jobRepository.findById(id).orElseThrow(()-> new RuntimeException("Job not found!"));
        return convertToDto(job);
    }

    @Override
    public boolean deleteJobById(Long id){
        if(!jobRepository.existsById(id)){return false;}
        jobRepository.deleteById(id);
        return true;
    }
}
