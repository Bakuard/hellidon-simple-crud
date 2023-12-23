package com.bakuard.simpleCrud.conf;

import com.bakuard.simpleCrud.controller.ErrorController;
import com.bakuard.simpleCrud.controller.GroupController;
import com.bakuard.simpleCrud.controller.StudentController;
import com.bakuard.simpleCrud.dal.GroupRepository;
import com.bakuard.simpleCrud.dal.PageMeta;
import com.bakuard.simpleCrud.dal.StudentRepository;
import com.bakuard.simpleCrud.dal.impl.GroupRepositoryImpl;
import com.bakuard.simpleCrud.dal.impl.StudentRepositoryImpl;
import com.bakuard.simpleCrud.exception.ApplicationConfigNotFound;
import com.bakuard.simpleCrud.service.GroupService;
import com.bakuard.simpleCrud.service.StudentService;
import com.bakuard.simpleCrud.service.TransactionUtil;
import com.bakuard.simpleCrud.service.ValidatorUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.helidon.http.media.MediaContext;
import io.helidon.http.media.jackson.JacksonSupport;
import io.helidon.openapi.OpenApiFeature;
import io.helidon.webserver.WebServer;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(DIContainer.class.getName());

    private final Properties properties;
    private final Map<String, Object> singletons;

    public DIContainer(String relativePathToPropertiesFile) {
        Properties properties = new Properties();

        try {
            InputStream inputStream = getClass()
                    .getClassLoader()
                    .getResourceAsStream(relativePathToPropertiesFile);
            properties.load(inputStream);
        } catch(IOException e) {
            throw new ApplicationConfigNotFound(
                    "Fail to load application config file by path: " + relativePathToPropertiesFile, e);
        }

        this.properties = properties;
        this.singletons = new HashMap<>();
    }

    public void startContainer() {
        try {
            flyway().migrate();
            webServer().start();
        } catch(RuntimeException e) {
            logger.error("Fail to start application", e);
            throw e;
        }
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

    public ValidatorUtil validatorUtil() {
        return (ValidatorUtil) singletons.computeIfAbsent("validatorUtil", key -> {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            return new ValidatorUtil(validator);
        });
    }

    public TransactionUtil transactionUtil() {
        return new TransactionUtil(transactionTemplate());
    }

    public StudentService studentService() {
        TransactionUtil transactionUtil = transactionUtil();
        ValidatorUtil validatorUtil = validatorUtil();
        StudentRepository studentRepository = studentRepository();

        return (StudentService) singletons.computeIfAbsent("studentService",
                key -> new StudentService(
                        transactionUtil,
                        validatorUtil,
                        studentRepository
                )
        );
    }

    public GroupService groupService() {
        TransactionUtil transactionUtil = transactionUtil();
        ValidatorUtil validatorUtil = validatorUtil();
        GroupRepository groupRepository = groupRepository();

        return (GroupService) singletons.computeIfAbsent("groupService",
                key -> new GroupService(
                        transactionUtil,
                        validatorUtil,
                        groupRepository
                )
        );
    }

    public ObjectMapper objectMapper() {
        class PageMetaSerializer extends JsonSerializer<PageMeta> {
            @Override
            public void serialize(PageMeta pageMeta, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                    throws IOException {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeNumberField("pageSize", pageMeta.pageSize());
                jsonGenerator.writeNumberField("pageNumber", pageMeta.pageNumber());
                jsonGenerator.writeNumberField("totalItems", pageMeta.totalItems());
                jsonGenerator.writeNumberField("lastPageNumber", pageMeta.lastPageNumber());
                jsonGenerator.writeNumberField("totalPages", pageMeta.totalPages());
                jsonGenerator.writeEndObject();
            }
        }

        return new ObjectMapper()
                .registerModule(
                        new SimpleModule().addSerializer(PageMeta.class, new PageMetaSerializer())
                );
    }

    public StudentController studentController() {
        return new StudentController(studentService(), groupService());
    }

    public GroupController groupController() {
        return new GroupController(groupService());
    }

    public ErrorController errorController() {
        return new ErrorController();
    }

    public WebServer webServer() {
        return WebServer.builder()
                .host("localhost")
                .port(8080)
                .addFeature(OpenApiFeature.builder()
                        .webContext("/api")
                        .name("/Students Service")
                        .build())
                .routing(routing ->
                        routing.register("/groups", groupController())
                                .register("/students", studentController())
                                .error(Throwable.class, errorController())
                )
                .mediaContext(MediaContext.builder()
                        .mediaSupportsDiscoverServices(false)
                        .addMediaSupport(JacksonSupport.create(objectMapper()))
                        .build())
                .build();
    }
}
