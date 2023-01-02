package org.prgrms.kdt.customer;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.sql.DataSource;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import com.wix.mysql.EmbeddedMysql;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.ScriptResolver.classPathScript;
import static com.wix.mysql.distribution.Version.v5_7_latest;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.config.Charset.UTF8;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@SpringJUnitConfig
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerNamedJdbcRepositoryTest {
    private static final Logger logger = LoggerFactory.getLogger(CustomerNamedJdbcRepositoryTest.class);
    @Configuration
    @ComponentScan(
            basePackages = {"org.prgrms.kdt.customer"}
    )
    static class Config{

        @Bean
        public DataSource dataSource(){
            /***
             return new EmbeddedDatabaseBuilder()
             .generateUniqueName(true)
             .setType(H2)
             .setScriptEncoding("UTF-8")
             .ignoreFailedDrops(true)
             .addScript("schema.sql")
             .build();
             ***/
            var dataSource = DataSourceBuilder.create()
                    .url("jdbc:mysql://localhost:2215/test-order_mgmt")
                    .username("test")
                    .password("test1234!")
                    .type(HikariDataSource.class)
                    .build();
//            dataSource.setMaximumPoolSize(1000);
//            dataSource.setMinimumIdle(100);
            return dataSource;
        }

        @Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource){
            return new JdbcTemplate(dataSource);
        }
        @Bean
        public NamedParameterJdbcTemplate namedParameterJdbcTemplate(JdbcTemplate jdbcTemplate){
            return new NamedParameterJdbcTemplate(jdbcTemplate);
        }

    }
    @Autowired
    CustomerNamedJdbcRepository customerJdbcRepository;
    @Autowired
    DataSource dataSource;
    Customer newCustomer;
    EmbeddedMysql embeddedMysql;

    @BeforeAll
    void setup(){
        newCustomer = new Customer(UUID.randomUUID(),
                "test-user",
                "test-user@gmail.com",
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        var mysqlConfig = aMysqldConfig(v5_7_latest)
                .withCharset(UTF8)
                .withPort(2215)
                .withUser("test", "test1234!")
                .withTimeZone("Asia/Seoul")
                .build();
        embeddedMysql = anEmbeddedMysql(mysqlConfig)
                .addSchema("test-order_mgmt", classPathScript("schema.sql"))
                .start();
        //customerJdbcRepository.deleteAll();
    }


    @AfterAll
    void cleanup(){
        embeddedMysql.stop();
    }

    @Test
    @Order(1)
    public void testHikariConnectionPool(){
        assertThat(dataSource.getClass().getName(), is("com.zaxxer.hikari.HikariDataSource"));
    }

    @Test
    @Order(2)
    @DisplayName("고객을 추가할 수 있다.")
    public void testInsert() {

        try {
            customerJdbcRepository.insert(newCustomer);
        }catch (BadSqlGrammarException e){
            logger.error("Got BadSqlGrammarException error code -> {}", e.getSQLException().getErrorCode(), e);
        }

        var retrievedCustomer = customerJdbcRepository.findById(newCustomer.getCustomerId());
        System.out.println(newCustomer.getCreatedAt());
        assertThat(retrievedCustomer.isEmpty(), is(false));
        assertThat(retrievedCustomer.get(), samePropertyValuesAs(newCustomer));
    }

    @Test
    @Order(3)
    @DisplayName("전체 고객을 조회할 수 있다")
    public void testFindAll() throws InterruptedException {
        var customers = customerJdbcRepository.findAll();
        assertThat(customers.isEmpty(), is(false));
    }

    @Test
    @Order(4)
    @DisplayName("이름으로 고객을 조회할 수 있다.")
    public void testFindByName() throws InterruptedException {
        var customers = customerJdbcRepository.findByName(newCustomer.getName());
        assertThat(customers.isEmpty(), is(false));

        var unknown = customerJdbcRepository.findByName("unknown-user");
        assertThat(unknown.isEmpty(), is(true));

    }

    @Test
    @Order(5)
    @DisplayName("이메일로 고객을 조회할 수 있다.")
    public void testFindByEmail() throws InterruptedException {
        var customers = customerJdbcRepository.findByByEmail(newCustomer.getEmail());
        assertThat(customers.isEmpty(), is(false));

        var unknown = customerJdbcRepository.findByByEmail("asf@naver.com");
        assertThat(unknown.isEmpty(), is(true));

    }

    @Test
    @Order(6)
    @DisplayName("고객을 수정할 수 있다")
    public void testUpdate(){
        newCustomer.changeName("updated-user");
        customerJdbcRepository.update(newCustomer);

        var all = customerJdbcRepository.findAll();
        assertThat(all, hasSize(1));
        assertThat(all, everyItem(samePropertyValuesAs(newCustomer)));

        var retrievedCustomer = customerJdbcRepository.findById(newCustomer.getCustomerId());
        assertThat(retrievedCustomer.isEmpty(), is(false));
        assertThat(retrievedCustomer.get(), samePropertyValuesAs(newCustomer));

        var unknown = customerJdbcRepository.findByByEmail("unknown-user@gmail.com");
        assertThat(unknown.isEmpty(), is(true));
    }
}
