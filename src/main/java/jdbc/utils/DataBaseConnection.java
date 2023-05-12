package jdbc.utils;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.*;
import java.util.Properties;

public class DataBaseConnection {

    private static final String DB_PROPERTIES = "db.properties";
    private static final String DB_URL = "db.url";
    private static final String DB_USER = "db.username";
    private static final String DB_PASS = "db.password";

    public static Properties loadProperties() {
        try (InputStream is = DataBaseConnection.class.getClassLoader().getResourceAsStream(DB_PROPERTIES)){
            Properties dbProperties = new Properties();
            dbProperties.load(is);
            return dbProperties;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static DataSource initDataSource() {
        Properties properties = loadProperties();
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL(properties.getProperty(DB_URL));
        dataSource.setUser(properties.getProperty(DB_USER));
        dataSource.setPassword(properties.getProperty(DB_PASS));
        return dataSource;
    }

}
