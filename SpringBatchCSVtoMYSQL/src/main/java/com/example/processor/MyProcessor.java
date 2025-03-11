package com.example.processor;

import org.springframework.batch.item.ItemProcessor;

import com.example.model.Student;

public class MyProcessor implements ItemProcessor<Student, Student>{

	@Override
	public Student process(Student item) throws Exception {
		// TODO Auto-generated method stub
		var m=item.getMat();
		var p=item.getPhy();
		var c=item.getChe();
		try
		{
		  double per=(m+p+c)/3;
		  item.setPer(per);
		  if(per>90) item.setStatus("EXCELLENT");
		  else if(per>80) item.setStatus("GOOD");
		  else if(per>=36) item.setStatus("PASS");
		  else item.setStatus("FAIL");
		}
		catch (Exception e) {
			// TODO: handle exception
			item.setStatus("ERROR");
		}
		return item;
	}
}
