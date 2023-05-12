package jdbc;

import jdbc.entity.Homework;
import jdbc.entity.Lesson;
import jdbc.operations.impl.LessonDao;
import jdbc.operations.LessonDaoOperations;
import jdbc.utils.DataBaseConnection;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        LessonDaoOperations<Lesson, Long> lessonDaoOperations = new LessonDao(DataBaseConnection.initDataSource());

        Homework homework = new Homework();
        homework.setId(2L);

        Lesson lesson = new Lesson();
        lesson.setName("PY");
        lesson.setHomework(homework);


        boolean isAdded = lessonDaoOperations.addLesson(lesson);
        System.out.println(isAdded);


        boolean isDeleted = lessonDaoOperations.deleteLesson(2L);
        System.out.println(isDeleted);

        List<Lesson> lessons = lessonDaoOperations.getAllLessons();
        System.out.println(lessons);

        Lesson lesson1 = lessonDaoOperations.getLesson(5L);
        System.out.println(lesson1);

    }
}
