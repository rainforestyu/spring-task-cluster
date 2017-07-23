package com.slarfsoft.springtaskcluster.repository;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.slarfsoft.springtaskcluster.domain.JobRecord;

import javax.persistence.LockModeType;
import java.sql.Date;
import java.util.List;

/**
 * @Auther Zhaoyu Chen @ Fuzhou
 * @Date 2017/7/21 0021.
 */
@Repository
public interface JobRepository extends JpaRepository<JobRecord, Long> {
    List<JobRecord> findAllByJobName(String jobName);

    JobRecord findByJobNameAndJobDate(String jobName, Date jobDate);
}
