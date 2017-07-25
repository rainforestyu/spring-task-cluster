package com.slarfsoft.springtaskcluster.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.sql.Date;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @Auther Zhaoyu Chen @ Fuzhou
 * @Date 2017/7/21 0021.
 */
@Entity
@Table(name = "job_record" , uniqueConstraints = {
        @UniqueConstraint(columnNames = {"job_name", "job_date"}) })
// Add this annotation to transfer "null" to front end
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class JobRecord implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "job_name", length = 255, nullable = false)
    private String jobName;

    @NotNull
    @Column(name = "job_date", nullable = false)
    private Date jobDate;

    @NotNull
    @Column(name = "lock_status", nullable = false)
    private Boolean lock_status;

    @Column(name = "job_start_time")
    private Timestamp startedDate;

    @Column(name = "job_end_time")
    private Timestamp endDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Date getJobDate() {
        return jobDate;
    }

    public void setJobDate(Date jobDate) {
        this.jobDate = jobDate;
    }

    public Boolean getLock_status() {
        return lock_status;
    }

    public void setLock_status(Boolean lock_status) {
        this.lock_status = lock_status;
    }

    public Timestamp getStartedDate() {
        return startedDate;
    }

    public void setStartedDateNow() {
        this.startedDate = Timestamp.valueOf(LocalDateTime.now());
    }

    public void setStartedDate(Timestamp startedDate) {
        this.startedDate = startedDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDateNow() {
        this.endDate =Timestamp.valueOf(LocalDateTime.now());
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }
}
