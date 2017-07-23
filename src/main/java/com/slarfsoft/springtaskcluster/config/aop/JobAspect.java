package com.slarfsoft.springtaskcluster.config.aop;

import com.slarfsoft.springtaskcluster.repository.JobRepository;
import com.slarfsoft.springtaskcluster.service.JobLockService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.Date;
import java.util.Arrays;

/**
 * Aspect for job locking execution of service and repository Spring components.
 *
 */
@Aspect
public class JobAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Environment env;

    @Autowired
    private JobLockService jobLockService;

    public JobAspect(Environment env) {
        this.env = env;
    }

    /**
     * Pointcut that matches all job with dayEnd Begin.
     */
    @Pointcut("execution(* com.slarfsoft.springtaskcluster.dayjob..dayEnd*(java.sql.Date) )")
    public void jobPackagePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * Advice that locked methods throwing exceptions.
     *
     * @param joinPoint join point for advice
     * @param e exception
     */
    @AfterThrowing(pointcut = "jobPackagePointcut()", throwing = "e")
    public void jobAfterThrowing(JoinPoint joinPoint, Throwable e) {
            log.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), e.getCause() != null? e.getCause() : "NULL");

    }

    /**
     * Advice that locked table when a method is entered and exited.
     *
     * @param joinPoint join point for advice
     * @return result
     * @throws Throwable throws IllegalArgumentException
     */
    @Around("jobPackagePointcut()")
    public Object jobAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        //parse getArgs
        String jobName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        final Date jobDate;
        if(args.length >0 && args[0] instanceof java.sql.Date)
            jobDate = (java.sql.Date)args[0];
        else
            throw  new IllegalArgumentException("the args is not a date type");
        log.info("jobName: "+jobName + "  jobDate: "+ jobDate.toString());
        try {
            //lock job recored
            if(jobLockService.tryLock(jobName,jobDate)==false) {
                log.info("The Job {} is running,",jobName);
                return null;
            }
            //run our job
            Object result = joinPoint.proceed();
            //unlock job record
            jobLockService.tryUnlock(jobName,jobDate);
            log.info("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), result);

            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        }catch (InterruptedException e){
            //unlock job record ??
            jobLockService.tryUnlock(jobName,jobDate);
            throw e;
        }
    }
}
