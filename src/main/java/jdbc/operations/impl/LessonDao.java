package jdbc.operations.impl;
import jdbc.entity.Homework;
import jdbc.entity.Lesson;
import jdbc.exceptions.JbdcOperationException;
import jdbc.operations.GenericDaoOperations;
import jdbc.operations.HomeworkDao;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LessonDao implements GenericDaoOperations<Lesson, Long> {
    private DataSource dataSource;
    private HomeworkDao homeworkDao;
    private final String SQL_INSERT = """
            INSERT INTO lesson (name, homework_id) VALUES (?, ?)
            """;
    private final String SQL_DELETE = """
            DELETE FROM lesson WHERE id = ?
            """;

    private final String SQL_SELECT = """
            SELECT id, name, homework_id, updated_at FROM lesson
            """;

    private final String SQL_SELECT_BY_ID = """
            SELECT id, name, homework_id, updated_at FROM lesson WHERE id = ?
            """;

    public LessonDao(final DataSource dataSource) {
        this.dataSource = dataSource;
        this.homeworkDao = new HomeworkDao(this.dataSource);
    }

    @Override
    public Lesson addLesson(final Lesson lesson) {
        Objects.requireNonNull(lesson);
        if (lesson.getId() != null) {
            throw new JbdcOperationException("Id must not be provided for insert operation");
        }

        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            int idx = 1;
            ps.setString(idx++, lesson.getName());
            ps.setLong(idx, lesson.getHomework().getId());

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted < 1) {
                throw new JbdcOperationException("No rows were added into database, input lesson = %s".formatted(lesson));
            }
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                Long id = generatedKeys.getLong("id");
                lesson.setId(id);

                String name = generatedKeys.getString("name");
                lesson.setName(name);

                Long homeworkId = generatedKeys.getLong("homework_id");

                Homework homework = homeworkDao.getHomeworkById(homeworkId);
                lesson.setHomework(homework);

                Date date = generatedKeys.getDate("updated_at");
                lesson.setUpdatedAt(date);
            }
            return lesson;
        } catch (SQLException e) {
            throw new JbdcOperationException("Failed to add lesson = %s".formatted(lesson), e);
        }
    }

    @Override
    public boolean deleteLesson(final Long id) {
        Objects.requireNonNull(id);

        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_DELETE)) {
            int idx = 1;
            ps.setLong(idx, id);

            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted < 1) {
                throw new JbdcOperationException("No rows were deleted from database");
            }
            return true;
        } catch (SQLException e) {
            throw new JbdcOperationException("Failed to delete lesson by id = %d".formatted(id), e);
        }
    }

    @Override
    public List<Lesson> getAllLessons() {
        List<Lesson> allLessons = new ArrayList<>();

        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_SELECT)) {

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Long homeworkId = resultSet.getLong("homework_id");

                Homework homework = homeworkDao.getHomeworkById(homeworkId);

                Lesson lesson = new Lesson();
                lesson.setId(resultSet.getLong("id"));
                lesson.setName(resultSet.getString("name"));
                lesson.setHomework(homework);
                lesson.setUpdatedAt(resultSet.getDate("updated_at"));
                allLessons.add(lesson);
            }
        } catch (SQLException e) {
            throw new JbdcOperationException("Failed to select lessons from database", e);
        }
        return allLessons;
    }

    @Override
    public Lesson getLesson(final Long id) {
        Objects.requireNonNull(id);

        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_ID)) {

            int idx = 1;
            preparedStatement.setLong(idx, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Long homeworkId = resultSet.getLong("homework_id");

                Homework homework = homeworkDao.getHomeworkById(homeworkId);

                Lesson lesson = new Lesson();
                lesson.setId(resultSet.getLong("id"));
                lesson.setName(resultSet.getString("name"));
                lesson.setHomework(homework);
                lesson.setUpdatedAt(resultSet.getDate("updated_at"));
                return lesson;
            } else {
                throw new JbdcOperationException("Lesson by id = %d was not fount".formatted(id));
            }
        } catch (SQLException e) {
            throw new JbdcOperationException("Failed to select lesson from database", e);
        }
    }

}
