package jdbc;

import jdbc.entity.Homework;
import jdbc.entity.Lesson;
import jdbc.operations.impl.GenericDao;
import jdbc.operations.GenericDaoOperations;
import jdbc.utils.DataBaseConnection;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        GenericDaoOperations<Lesson, Long> lessonDaoOperations = new GenericDao(DataBaseConnection.initDataSource());

        Homework homework = new Homework();
        homework.setId(2L);


        Lesson lesson = new Lesson();
        lesson.setName("Math2");
        lesson.setHomework(homework);


        boolean isAdded = lessonDaoOperations.addLesson(lesson);
        System.out.println(isAdded);


        boolean isDeleted = lessonDaoOperations.deleteLesson(5L);
        System.out.println(isDeleted);

        List<Lesson> lessons = lessonDaoOperations.getAllLessons();
        System.out.println(lessons);

        Lesson lesson1 = lessonDaoOperations.getLesson(5L);
        System.out.println(lesson1);

    }
}
