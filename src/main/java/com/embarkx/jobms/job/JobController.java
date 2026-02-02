package com.embarkx.jobms.job;

import com.embarkx.jobms.job.dto.JobDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {
    private JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public List<JobDTO> findAll(){
        return jobService.findAll();
    }

    @PostMapping()
    public ResponseEntity<String> createJob(@RequestBody Job job){
        jobService.createJob(job);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDTO> getJobById(@PathVariable Long id){
        if(jobService.getJobById(id)!=null){
            return new ResponseEntity<>(jobService.getJobById(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteJobById(@PathVariable Long id){
        if(jobService.deleteJobById(id)!=false){
            return new ResponseEntity<>(jobService.deleteJobById(id), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

}
