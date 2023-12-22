package com.bakuard.simpleCrud.conf;

import com.bakuard.simpleCrud.dal.StudentRepository;
import com.bakuard.simpleCrud.dal.impl.StudentRepositoryImpl;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DIContainer {

    private final Properties properties;
    private final Map<String, Object> singletons;

    public DIContainer(Properties properties) {
        this.properties = properties;
        this.singletons = new HashMap<>();
    }

    public DataSource dataSource() {
        return (DataSource) singletons.computeIfAbsent("dataSource", key -> {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl(properties.getProperty("app.database.jdbcUrl"));
            hikariConfig.setAutoCommit(false);
            hikariConfig.setMaximumPoolSize(10);
            hikariConfig.setMinimumIdle(5);
            hikariConfig.setPoolName("hikariPool");

            return new HikariDataSource(hikariConfig);
        });
    }

    public StudentRepository studentRepository(DataSource dataSource) {
        return new StudentRepositoryImpl(dataSource);
    }

    public PlatformTransactionManager transactionManager() {
        return (PlatformTransactionManager) singletons.computeIfAbsent("transactionManager",
                key -> new DataSourceTransactionManager(dataSource())
        );
    }

    public TransactionTemplate transactionTemplate() {
        return new TransactionTemplate(transactionManager());
    }
}
