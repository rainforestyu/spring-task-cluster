package com.slarfsoft.springtaskcluster.web.rest;

import com.slarfsoft.springtaskcluster.dayjob.Job;
import com.slarfsoft.springtaskcluster.domain.JobRecord;
import com.slarfsoft.springtaskcluster.repository.JobRepository;
import com.slarfsoft.springtaskcluster.service.JobLockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * @Auther Zhaoyu Chen @ Fuzhou
 * @Date 2017/7/21 0021.
 */
@RestController
@RequestMapping("/api")
public class JobResource {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final JobRepository jobRepository;
    private final TaskExecutor taskExecutor;
    private final JobLockService jobLockService;

    @Autowired
    private Job job;

    public JobResource(@Qualifier("taskExecutor") TaskExecutor taskExecutor,JobRepository jobRepository,JobLockService jobLockService) {
        this.taskExecutor = taskExecutor;
        this.jobRepository = jobRepository;
        this.jobLockService = jobLockService;
   }

    @GetMapping("/job")
    public ResponseEntity<List<JobRecord>> getAll() {
        List<JobRecord> allJob = jobRepository.findAll();
        return new ResponseEntity<>(allJob, HttpStatus.OK);
    }

    @GetMapping("/job/{id}")
    public ResponseEntity<JobRecord> getJob(@PathVariable Long id) {
        JobRecord job = jobRepository.findOne(id);
        if(job == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(job,HttpStatus.OK);
    }

    @PostMapping("/job/reset")
    public ResponseEntity changeStatus(@RequestParam(required=true) String jobName ,@RequestParam(required=true) Date jobDate) {
        log.info("reset quest job Name:{}, job Date: {}",jobName,jobDate);

        if(jobLockService.tryUnlock(jobName,jobDate)){
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/job/execute")
    public ResponseEntity reRun(@RequestParam(required=true) String jobName ,@RequestParam(required=true) Date jobDate) {
        if(jobRepository.findByJobNameAndJobDate(jobName,jobDate) != null){
            taskExecutor.execute(()->{
                job.dayEndCal(jobDate);
            });
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.badRequest().build();
        }
    }
}
