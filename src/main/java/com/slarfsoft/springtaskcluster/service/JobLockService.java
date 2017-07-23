package com.slarfsoft.springtaskcluster.service;

import com.slarfsoft.springtaskcluster.domain.JobRecord;
import com.slarfsoft.springtaskcluster.repository.JobRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Date;

/**
 * @Auther Zhaoyu Chen @ Fuzhou
 * @Date 2017/7/21 0021.
 */
@Service
@Transactional
public class JobLockService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    JobRepository jobRepository;

    public JobLockService(JobRepository jobRepository){
        this.jobRepository = jobRepository;
    }

    public boolean tryLock(String jobName,Date jobDate){
        //get job detail
        JobRecord jobDetail = jobRepository.findByJobNameAndJobDate(jobName,jobDate);
        if(jobDetail == null) {
            //Create Job detail
            JobRecord job = new JobRecord();
            job.setJobName(jobName);
            job.setJobDate(jobDate);
            job.setStartedDateNow();
            job.setEndDate(null);
            job.setLock_status(true);

            jobRepository.saveAndFlush(job);
            return true;
        }
        else if(jobDetail.getLock_status() == false){
            jobDetail.setStartedDateNow();
            jobDetail.setEndDate(null);
            jobDetail.setLock_status(true);

            jobRepository.saveAndFlush(jobDetail);
            return true;
        }
        return false;
    }

    public boolean tryUnlock(String jobName,Date jobDate){
        //get job detail
        JobRecord jobDetail = jobRepository.findByJobNameAndJobDate(jobName,jobDate);
        if(jobDetail != null) {
            jobDetail.setLock_status(false);
            jobDetail.setEndDateNow();
            jobRepository.saveAndFlush(jobDetail);
        }
        return true;
    }
}
