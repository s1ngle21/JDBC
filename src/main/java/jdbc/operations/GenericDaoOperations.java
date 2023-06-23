package jdbc.operations;

import jdbc.entity.Lesson;

import java.util.List;

public interface GenericDaoOperations<T, ID> {
    Lesson addLesson(T lesson);
    boolean deleteLesson(ID id);
    List<T> getAllLessons();
    T getLesson(ID id);
}
