package ex17;

import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class MySqlContainerTest {

    @Container
    @ServiceConnection // o Spring Boot configura spring.datasource.* automaticamente a partir do container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.4");

    @Autowired DataSource dataSource;

    @Test
    void conectaNoContainer() throws Exception {
        try (var c = dataSource.getConnection()) {
            assertTrue(c.isValid(2));
        }
    }
}
