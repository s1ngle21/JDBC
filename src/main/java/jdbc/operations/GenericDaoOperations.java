package jdbc.operations;

import java.util.List;

public interface GenericDaoOperations<T, ID> {
    boolean addLesson(T lesson);
    boolean deleteLesson(ID id);
    List<T> getAllLessons();
    T getLesson(ID id);
}
