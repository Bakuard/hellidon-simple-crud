package com.bakuard.simpleCrud.conf;

import com.bakuard.simpleCrud.dal.GroupRepository;
import com.bakuard.simpleCrud.dal.StudentRepository;
import com.bakuard.simpleCrud.dal.impl.GroupRepositoryImpl;
import com.bakuard.simpleCrud.dal.impl.StudentRepositoryImpl;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DIContainer {

    private final Properties properties;
    private final Map<String, Object> singletons;

    public DIContainer(String relativePathToPropertiesFile) throws IOException {
        InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream(relativePathToPropertiesFile);
        Properties properties = new Properties();
        properties.load(inputStream);

        this.properties = properties;
        this.singletons = new HashMap<>();
    }

    public HikariDataSource dataSource() {
        return (HikariDataSource) singletons.computeIfAbsent("dataSource", key -> {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl(properties.getProperty("app.database.jdbcUrl"));
            hikariConfig.setAutoCommit(false);
            hikariConfig.setMaximumPoolSize(10);
            hikariConfig.setMinimumIdle(5);
            hikariConfig.setPoolName("hikariPool");

            return new HikariDataSource(hikariConfig);
        });
    }

    public PlatformTransactionManager transactionManager() {
        DataSource dataSource = dataSource();
        return (PlatformTransactionManager) singletons.computeIfAbsent("transactionManager",
                key -> new DataSourceTransactionManager(dataSource)
        );
    }

    public TransactionTemplate transactionTemplate() {
        return new TransactionTemplate(transactionManager());
    }

    public Flyway flyway() {
        return Flyway.configure().
                locations("classpath:db").
                dataSource(dataSource()).
                load();
    }

    public JdbcClient jdbcClient() {
        return JdbcClient.create(dataSource());
    }

    public StudentRepository studentRepository() {
        JdbcClient jdbcClient = jdbcClient();
        return (StudentRepository) singletons.computeIfAbsent("studentRepository",
                key -> new StudentRepositoryImpl(jdbcClient));
    }

    public GroupRepository groupRepository() {
        JdbcClient jdbcClient = jdbcClient();
        return (GroupRepository) singletons.computeIfAbsent("groupRepository",
                key -> new GroupRepositoryImpl(jdbcClient));
    }
}
