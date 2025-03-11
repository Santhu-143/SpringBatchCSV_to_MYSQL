package com.example.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class MyJobListener implements JobExecutionListener{

	private long start;
	@Override
	public void beforeJob(JobExecution je) {
		start=System.currentTimeMillis();
		System.out.println("BEFORE JOB : "+je.getStatus());
	}
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		System.out.println("AFTER JOB : "+jobExecution.getStatus());
		long end=System.currentTimeMillis();
		System.out.println("TIME TAKEN : "+(end-start));
	}
}
