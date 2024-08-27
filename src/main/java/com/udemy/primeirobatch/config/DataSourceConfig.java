package com.udemy.primeirobatch.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Primary
//    @Bean(name = "dfDataSource")
    @Bean(name = "dataSource")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:oracle:thin:@localhost:49161:xe")
                .password("oracle")
                .username("system")
                .driverClassName("oracle.jdbc.OracleDriver")
                .build();

//        return DataSourceBuilder.create()
//                .url("jdbc:mysql://localhost:3306/db")
//                .password("P@ssw0rd")
//                .username("user")
//                .driverClassName("com.mysql.cj.jdbc.Driver")
//                .build();
    }

//    @Bean
//    public JobRepository jobRepository(PlatformTransactionManager transactionManager) throws Exception {
//        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
//        factory.setTransactionManager(transactionManager);
//        factory.setIsolationLevelForCreate("ISOLATION_SERIALIZABLE");
//        factory.setMaxVarCharLength(255);
//        return factory.getObject();
//    }

//    @Bean
//    @Primary
//    public JpaTransactionManager jpaTransactionManager(DataSource dataSource) {
//        final var transactionManager = new JpaTransactionManager();
//        transactionManager.setDataSource(dataSource);
//        return transactionManager;
//    }
}
