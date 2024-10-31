package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Util {
    private static Connection connection = null;
    private static Util instance = null;
    private static final Logger logger = Logger.getLogger(Util.class.getName());

    private Util() {
        try {
            Properties props = getProps();
            connection = DriverManager.getConnection(
                    props.getProperty("db.url"),
                    props.getProperty("db.username"),
                    props.getProperty("db.password")
            );
        } catch (SQLException | IOException e) {
            logger.log(Level.SEVERE, "Database connection error", e);
        }
    }

    public static synchronized Util getInstance() {
        if (instance == null) {
            instance = new Util();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error closing connection", e);
            }
        }
    }

    private static Properties getProps() throws IOException {
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get(Util.class.getResource("/database.properties").toURI()))) {
            props.load(in);
        } catch (IOException | URISyntaxException e) {
            throw new IOException("Database config file not found", e);
        }
        return props;
    }

    private static SessionFactory sessionFactory = null;
    static {
        try {
            Properties settings = new Properties();
            settings.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/users");
            settings.setProperty("hibernate.connection.username", "root");
            settings.setProperty("hibernate.connection.password", "ENG2104!Contract");
            settings.setProperty("dialect", "org.hibernate.dialect.MySQLDialect");
            settings.setProperty("hibernate.hbm2ddl.auto", "create");

            sessionFactory = new org.hibernate.cfg.Configuration()
                    .addProperties(settings)
                    .addAnnotatedClass(User.class)
                    .buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Session getSession() throws HibernateException {
        return sessionFactory.openSession();
    }

    public static void close() throws HibernateException {
        getInstance().close();
    }
}
