package com.slarfsoft.springtaskcluster;

import com.slarfsoft.springtaskcluster.repository.JobRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringTaskClusterApplicationTests {
	@Autowired
	JobRepository jobRepository;

	@Test
	public void contextLoads() {
	}

	@Test
	public void jobAdd() {
		jobRepository.findAllByJobName("abc");
	}
}
