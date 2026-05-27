package mthree.com.fullstackschool.service;

import mthree.com.fullstackschool.dao.StudentDao;
import mthree.com.fullstackschool.model.Course;
import mthree.com.fullstackschool.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentServiceInterface {

    //YOUR CODE STARTS HERE

    @Autowired
    StudentDao studentDao;

    @Autowired
    CourseServiceImpl courseService;

    // for tests
    public StudentServiceImpl(StudentDao studentDao){
        this.studentDao = studentDao;
    }

    public StudentServiceImpl(StudentDao studentDao, CourseServiceImpl courseService) {
        this.studentDao = studentDao;
        this.courseService = courseService;
    }

    //YOUR CODE ENDS HERE

    public List<Student> getAllStudents() {
        //YOUR CODE STARTS HERE

        return studentDao.getAllStudents();

        //YOUR CODE ENDS HERE
    }

    public Student getStudentById(int id) {
        //YOUR CODE STARTS HERE

        try {
            return studentDao.findStudentById(id);
        } catch (DataAccessException e) {
            Student student = new Student();
            student.setStudentFirstName("Student Not Found");
            student.setStudentLastName("Student Not Found");
            return student;
        }

        //YOUR CODE ENDS HERE
    }

    public Student addNewStudent(Student student) {
        //YOUR CODE STARTS HERE

        boolean fNameBlank = (student.getStudentFirstName() == null || student.getStudentFirstName().isBlank());
        boolean lNameBlank = (student.getStudentLastName() == null || student.getStudentLastName().isBlank());

        // i can't put this in one big if statement because the requirements say 'or' not 'and'
        if (fNameBlank) student.setStudentFirstName("First Name blank, student NOT added");
        if (lNameBlank) student.setStudentLastName("Last Name blank, student NOT added");
        if (fNameBlank || lNameBlank) return student;

        return studentDao.createNewStudent(student);

        //YOUR CODE ENDS HERE
    }

    public Student updateStudentData(int id, Student student) {
        //YOUR CODE STARTS HERE

        if (id != student.getStudentId()) {
            student.setStudentFirstName("IDs do not match, student not updated");
            student.setStudentLastName("IDs do not match, student not updated");
            return student;
        }

        studentDao.updateStudent(student);
        return studentDao.findStudentById(id);

        //YOUR CODE ENDS HERE
    }

    public void deleteStudentById(int id) {
        //YOUR CODE STARTS HERE

        studentDao.deleteStudent(id);

        //YOUR CODE ENDS HERE
    }

    public void deleteStudentFromCourse(int studentId, int courseId) {
        //YOUR CODE STARTS HERE

        Student student = getStudentById(studentId);
        Course course = courseService.getCourseById(courseId);

        if (student.getStudentFirstName().equals("Student Not Found")) System.out.println(student.getStudentFirstName());
        else if (course.getCourseName().equals("Course Not Found")) System.out.println(student.getStudentLastName());
        else {
            studentDao.deleteStudentFromCourse(studentId, courseId);
            System.out.println("Student: " + studentId + " deleted from course: " + courseId);
        }

        //YOUR CODE ENDS HERE
    }

    public void addStudentToCourse(int studentId, int courseId) {
        //YOUR CODE STARTS HERE

        Student student = getStudentById(studentId);
        Course course = courseService.getCourseById(courseId);

        if (student.getStudentFirstName().equals("Student Not Found")) System.out.println(student.getStudentFirstName());
        else if (course.getCourseName().equals("Course Not Found")) System.out.println(student.getStudentLastName());
        else {
            try {
                studentDao.addStudentToCourse(studentId, courseId);
                System.out.println("Student: " + studentId + " added to course: " + courseId);
            } catch (Exception e) {
                System.out.println("Student: " + studentId + " already enrolled in course: " + courseId);
            }
        }
        //YOUR CODE ENDS HERE
    }
}
