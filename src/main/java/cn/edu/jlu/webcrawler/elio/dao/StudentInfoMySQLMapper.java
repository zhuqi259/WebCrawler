package cn.edu.jlu.webcrawler.elio.dao;

import cn.edu.jlu.webcrawler.elio.model.StudentInfo;
import org.springframework.stereotype.Service;

@Service("studentInfoMySQLMapper")
public interface StudentInfoMySQLMapper {
    int insertStudent(StudentInfo studentInfo);
}
