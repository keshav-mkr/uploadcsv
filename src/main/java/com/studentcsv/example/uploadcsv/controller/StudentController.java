package com.studentcsv.example.uploadcsv.controller;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.studentcsv.example.uploadcsv.model.Student;
import com.studentcsv.example.uploadcsv.repository.StudentRepository;
import com.studentcsv.example.uploadcsv.service.StudentService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/students")
public class StudentController {

    @Autowired
    StudentRepository studentRepository;
    private final StudentService service;

    @PostMapping(value = "/upload",consumes = {"multipart/form-data"})
    public ResponseEntity<Integer> uploadStuden(@RequestPart("file")MultipartFile file) throws IOException {
        System.out.println("Controller runing");
        return ResponseEntity.ok(service.uploadStudents(file));
    }

    @GetMapping("download")
    public ResponseEntity<Resource> downloadFile(){
        System.out.println("download runing");
        String fileName="student.csv";
        ByteArrayInputStream fileData=service.load();
        InputStreamResource resource = new InputStreamResource(fileData);
        return ResponseEntity.ok().header(HttpHeaders.
                CONTENT_DISPOSITION,"Attachment;filename"+fileName)
                .contentType(MediaType.parseMediaType("application/csv")).body(resource);

    }

    @GetMapping("showLogs")
    public ResponseEntity<Resource> testLogs(){
        log.trace("A TRACE Message");
        log.debug("A DEBUG Message");
        log.info("An INFO Message");
        log.warn("A WARN Message");
        log.error("An ERROR Message");

        return null;

    }


    @GetMapping("/csvexport")
    public void exportCSV(HttpServletResponse response)
            throws Exception {
        //set file name and content type
        String filename = "Student-data.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");
        //create a csv writer
        StatefulBeanToCsv<Student> writer = new StatefulBeanToCsvBuilder<Student>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(CSVWriter.DEFAULT_SEPARATOR).withOrderedResults(false)
                .build();
        //write all employees data to csv file
        List<Student> studentList = studentRepository.findAll();
        System.out.println("Student size is "+studentList.size());
        writer.write(studentRepository.findAll());

    }
}
