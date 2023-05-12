package jdbc.operations;

import jdbc.entity.Homework;
import jdbc.exceptions.JbdcOperationException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class HomeworkDao {
    private DataSource dataSource;

    public HomeworkDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Homework getHomeworkById(Long id) {
        Objects.requireNonNull(id);
        String sql = """
                SELECT id, name, description FROM homework WHERE id = ?
                """;
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)){

            int idx = 1;
            ps.setLong(idx, id);

            ResultSet resultSet = ps.executeQuery();
            Homework homework = new Homework();
            if (resultSet.next()) {
                homework.setId(resultSet.getLong("id"));
                homework.setName(resultSet.getString("name"));
                homework.setDescription(resultSet.getString("description"));
            } else {
                throw new JbdcOperationException("jdbc.exceptions.Homework with id = %d not found".formatted(id));
            }
            return homework;
        } catch (SQLException e) {
            throw new JbdcOperationException("Failed to select homework by id = %d".formatted(id), e);
        }
    }
}
