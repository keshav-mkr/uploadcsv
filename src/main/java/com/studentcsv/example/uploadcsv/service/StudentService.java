package com.studentcsv.example.uploadcsv.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.studentcsv.example.uploadcsv.StudenrCsvRepresentation;
import com.studentcsv.example.uploadcsv.model.Student;
import com.studentcsv.example.uploadcsv.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

     public Integer uploadStudents(MultipartFile file) throws IOException {

        Set<Student> students= parsCsv(file);
        studentRepository.saveAll(students);
        return students.size();
    }

    private Set<Student> parsCsv(MultipartFile file) throws IOException {

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            HeaderColumnNameMappingStrategy<StudenrCsvRepresentation> strategy =
                    new HeaderColumnNameMappingStrategy<>();
            strategy.setType(StudenrCsvRepresentation.class);
            CsvToBean<StudenrCsvRepresentation> csvToBean=
                    new CsvToBeanBuilder<StudenrCsvRepresentation>(reader)
                    .withMappingStrategy(strategy)
                    .withIgnoreEmptyLine(true)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse()
                    .stream()
                    .map(csvLine -> Student.builder()
                    .firstname(csvLine.getFname())
                    .lastname(csvLine.getLname())
                    .age(csvLine.getAge())
                    .build()).collect(Collectors.toSet());
        }

    }

    public ByteArrayInputStream load() {
       List<Student> studentList= studentRepository.findAll();
       System.out.println("student size is "+studentList.size());
       ByteArrayInputStream stream = studentToCSV(studentList);
       return stream;

    }

  private ByteArrayInputStream studentToCSV(List<Student> studentList) {
        CSVFormat format =CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        try(ByteArrayOutputStream out = new ByteArrayOutputStream();
            CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out),format);){
            for(Student student : studentList){
                List<String> data = Arrays.asList(String.valueOf(student.getId()),
                        student.getFirstname(),student.getLastname(),
                        String.valueOf(student.getAge()));
                csvPrinter.printRecords(data);

            }
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {

        }
      return null;
    }

}

