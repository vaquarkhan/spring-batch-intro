package io.github.benas.sbi;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@Import(DataSourceConfiguration.class)
public class JobConfiguration {

	private DataSource dataSource;

	private PlatformTransactionManager transactionManager;

	@Autowired
	public JobConfiguration(DataSource dataSource, PlatformTransactionManager transactionManager) {
		this.dataSource = dataSource;
		this.transactionManager = transactionManager;
	}

	@Bean
	public JobRepository jobRepository() throws Exception {
		JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
		jobRepositoryFactoryBean.setDataSource(dataSource);
		jobRepositoryFactoryBean.setTransactionManager(transactionManager);
		return jobRepositoryFactoryBean.getObject();
	}

	@Bean
	public JobLauncher jobLauncher() throws Exception {
		SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
		simpleJobLauncher.setJobRepository(jobRepository());
		return simpleJobLauncher;
	}

	@Bean
	public Step step() throws Exception {
		return new StepBuilder("step")
				.repository(jobRepository())
				.transactionManager(transactionManager)
				.tasklet((contribution, chunkContext) -> {
					System.out.println("hello world");
					return RepeatStatus.FINISHED;
				}).build();
	}

	@Bean
	public Job job() throws Exception {
		return new JobBuilder("job")
				.repository(jobRepository())
				.start(step())
				.build();
	}

}
