package com.studentcsv.example.uploadcsv;

import com.studentcsv.example.uploadcsv.model.Student;
import com.studentcsv.example.uploadcsv.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class UploadcsvApplication implements CommandLineRunner {

	@Autowired
	StudentRepository studentRepository;

	public static void main(String[] args) {
		SpringApplication.run(UploadcsvApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		List<Student> students = new ArrayList<>();


		/*students.add(new Student(1,"Amit","Kumar",34));
		students.add(new Student(2,"Sagar","Sona",32));
		students.add(new Student(3,"Rahul","Kumar",30));
		students.add(new Student(4,"Roshan","Mathur",34));*/

		students=studentRepository.findAll();
		//studentRepository.saveAll(students);

	}


}
