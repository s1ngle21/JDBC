package jdbc.operations.impl;
import jdbc.entity.Homework;
import jdbc.entity.Lesson;
import jdbc.exceptions.JbdcOperationException;
import jdbc.operations.HomeworkDao;
import jdbc.operations.GenericDaoOperations;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GenericDao implements GenericDaoOperations<Lesson, Long> {
    private DataSource dataSource;

    public GenericDao(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean addLesson(final Lesson lesson) {
        Objects.requireNonNull(lesson);
        if (lesson.getId() != null) {
            throw new JbdcOperationException("Id must not be provided for while adding operation");
        }
        String sql = """ 
                INSERT INTO lesson (name, homework_id) VALUES (?, ?)
                """;
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int idx = 1;
            ps.setString(idx++, lesson.getName());
            ps.setLong(idx, lesson.getHomework().getId());

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted < 1) {
                throw new JbdcOperationException("No rows were added into database, input lesson = %s".formatted(lesson));
            }
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                Long id = generatedKeys.getLong(1);
                lesson.setId(id);
            }
            return true;
        } catch (SQLException e) {
            throw new JbdcOperationException("Failed to add lesson = %s".formatted(lesson), e);
        }
    }

    @Override
    public boolean deleteLesson(final Long id) {
        Objects.requireNonNull(id);
        String sql = """
                DELETE FROM lesson WHERE id = ?
                """;
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
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
        String sql = """
                SELECT id, name, homework_id FROM lesson
                """;
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Long homework_id = resultSet.getLong("homework_id");

                HomeworkDao homeworkDao = new HomeworkDao(this.dataSource);
                Homework homework = homeworkDao.getHomeworkById(homework_id);

                Lesson lesson = new Lesson();
                lesson.setId(resultSet.getLong("id"));
                lesson.setName(resultSet.getString("name"));
                lesson.setHomework(homework);
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
        String sql = """
                SELECT id, name, homework_id FROM lesson WHERE id = ?
                """;
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            int idx = 1;
            preparedStatement.setLong(idx, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Long homework_id = resultSet.getLong("homework_id");

                HomeworkDao homeworkDao = new HomeworkDao(this.dataSource);
                Homework homework = homeworkDao.getHomeworkById(homework_id);

                Lesson lesson = new Lesson();
                lesson.setId(resultSet.getLong("id"));
                lesson.setName(resultSet.getString("name"));
                lesson.setHomework(homework);
                return lesson;
            } else {
                throw new JbdcOperationException("jdbc.exceptions.Lesson by id = %d was not fount".formatted(id));
            }
        } catch (SQLException e) {
            throw new JbdcOperationException("Failed to select lesson from database", e);
        }
    }

}
