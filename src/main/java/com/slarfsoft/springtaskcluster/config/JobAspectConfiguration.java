package com.slarfsoft.springtaskcluster.config;

import com.slarfsoft.springtaskcluster.config.aop.JobAspect;
import com.slarfsoft.springtaskcluster.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;

@Configuration
@EnableAspectJAutoProxy
public class JobAspectConfiguration {

    @Bean
    public JobAspect loggingAspect(Environment env) {
        return new JobAspect(env);
    }
}
