package jdbc;

import jdbc.entity.Homework;
import jdbc.entity.Lesson;
import jdbc.operations.impl.LessonDao;
import jdbc.operations.GenericDaoOperations;
import jdbc.utils.DataBaseConnection;

import java.util.List;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) {
        GenericDaoOperations<Lesson, Long> lessonDaoOperations = new LessonDao(DataBaseConnection.initDataSource());

        Homework homework = new Homework();
        homework.setId(2L);


        Lesson lesson = new Lesson();
        lesson.setName("PY");
        lesson.setHomework(homework);


        Lesson addedLesson = lessonDaoOperations.addLesson(lesson);
        LOGGER.info("Added lesson: " + addedLesson);


        boolean isDeleted = lessonDaoOperations.deleteLesson(2L);
        LOGGER.info(Boolean.toString(isDeleted));

        List<Lesson> lessons = lessonDaoOperations.getAllLessons();
        LOGGER.info("" + lessons);

        Lesson lesson1 = lessonDaoOperations.getLesson(1L);
        LOGGER.info("" + lesson1);

    }
}
