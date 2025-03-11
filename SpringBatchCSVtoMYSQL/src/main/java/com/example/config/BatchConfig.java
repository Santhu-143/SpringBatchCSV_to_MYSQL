package com.example.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.listener.MyJobListener;
import com.example.model.Student;
import com.example.processor.MyProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	// 1. Reader
	@Bean
	public ItemReader<Student> reader() {
		System.out.println("IN READER -->");
		FlatFileItemReader<Student> reader = new FlatFileItemReader<>();
		reader.setResource(new ClassPathResource("students.csv"));

		// Read data line by line
		reader.setLineMapper(new DefaultLineMapper<>() {
			{
				// Tokenize data based on delimiter and provide names for one line
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setDelimiter(",");
						setNames("id", "name", "mat", "phy", "che");
					}
				});

				// Convert line to object and provide data to variables
				setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {
					{
						setTargetType(Student.class);
					}
				});
			}
		});

		return reader;
	}

	// 2. Processor
	@Bean
	public ItemProcessor<Student, Student> processor() {
		return new MyProcessor();
	}

	// 3. Writer
	@Bean
	public ItemWriter<Student> writer() {
		System.out.println("AT WRITER");
		return new JdbcBatchItemWriterBuilder<Student>()
				.dataSource(ds())
				.sql("INSERT INTO STUDENTS(id,name,mat,phy,che,per,status) VALUES(:id,:name,:mat,:phy,:che,:per,:status)")
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
				.build();
	}

	// 4. Step object
	@Bean
	public Step stepA() throws Exception {
		return new StepBuilder("stepA", jobRepository())
				.<Student, Student>chunk(3, transactionManager())
				.reader(reader())
				.writer(writer())
				.processor(processor())
				.build();
	}

	// 5. Listener object
	@Bean
	public JobExecutionListener listener() {
		return new MyJobListener();
	}

	// 6. Job object
	@Bean
	public Job jobA() throws Exception {
		return new JobBuilder("studentJob", jobRepository())
				.start(stepA())
				.listener(listener())
				.build();
	}

	// 7. Datasource object
	@Bean(name = "dataSource")  // Explicitly name the bean
	@Primary
	public DataSource ds() {
	    System.out.println("Creating MySQL datasource...");
	    DriverManagerDataSource ds = new DriverManagerDataSource();
	    ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
	    ds.setUrl("jdbc:mysql://localhost:3306/csv_sql");
	    ds.setUsername("root");
	    ds.setPassword("root");
	    return ds;
	}
	
	// 8. Transaction manager
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(ds());
	}
	
	// 9. Job repository
	@Bean
	public JobRepository jobRepository() throws Exception {
		JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
		factory.setDataSource(new EmbeddedDatabaseBuilder()
	            .setType(EmbeddedDatabaseType.H2)
	            .addScript("classpath:org/springframework/batch/core/schema-h2.sql")
	            .build());
		factory.setTransactionManager(transactionManager());
		factory.setIsolationLevelForCreate("ISOLATION_REPEATABLE_READ");
		factory.afterPropertiesSet();
		return factory.getObject();
	}
	
//	@Bean
//	public JobRepository jobRepository() throws Exception {
//	    JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
//	    factory.setDataSource(new EmbeddedDatabaseBuilder()
//	            .setType(EmbeddedDatabaseType.H2)
//	            .addScript("classpath:org/springframework/batch/core/schema-h2.sql")
//	            .build());
//	    factory.setTransactionManager(new DataSourceTransactionManager(ds()));
//	    factory.afterPropertiesSet();
//	    return factory.getObject();
//	}
//
//	@Bean
//	public PlatformTransactionManager transactionManager() {
//	    return new ResourcelessTransactionManager();
//	}
}