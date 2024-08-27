package com.udemy.primeirobatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.function.FunctionItemProcessor;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.Iterator;
import java.util.List;

@Configuration
//@EnableBatchProcessing()
public class BatchConfig {

    private  final DataSource dataSource;
    private final PlatformTransactionManager transactionManager;
    private final JobRepository jobRepository;


    public BatchConfig(@Qualifier("dataSource") DataSource dataSource, TransactionTemplate transactionTemplate, PlatformTransactionManager transactionManager, JobRepository jobRepository) {
        this.dataSource = dataSource;
        this.transactionManager = transactionManager;
        this.jobRepository = jobRepository;
    }

// Descomentar esse codigo abaixo para simular  (java.lang.IllegalArgumentException: Job name must be specified in case of multiple jobs)
//    @Bean
//    public Job jobDuplicado(final Step stepImprime, final JobRepository jobRepository) {
//        System.out.println("Iniciando o Job: duplicado");
//        return new JobBuilder("jobDuplicado", jobRepository)
//                .start(stepImprime)
//                .incrementer(new RunIdIncrementer())
//                .build();
//    }

    @Bean
    public Job imprimeOlaJob(final Step stepImprime, final JobRepository jobRepository) {
        System.out.println("Iniciando o Job: imprimeOlaJob");
        return new JobBuilder("imprimeOlaJob", jobRepository)
                .start(stepImprime)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step stepImprime() {
        System.out.printf("chamadoooooooooooooo");
        return new StepBuilder("stepImprime", jobRepository)
                .<Integer, String>chunk(1, transactionManager)
                .reader(contaAteDezReader())
                .processor(parOuImparProcessor())
                .faultTolerant()
                .writer(imprimeWrite())
                .taskExecutor(taskExecutor())
                .build();
    }

    private ItemReader<Integer> contaAteDezReader() {
        System.out.println("contaAteDezReader");
        Iterator<Integer> iterator = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).iterator();
        return new IteratorItemReader<>(iterator);
    }

    private FunctionItemProcessor<Integer, String> parOuImparProcessor() {
        System.out.println("parOuImparProcessor");
        return new FunctionItemProcessor<>(
                item -> {
                    System.out.printf("Processando: %d\n", item); // Log
                    return item % 2 == 0 ? String.format("Item %s é par", item) : String.format("Item %s é impar", item);
                }
        );
    }

    private ItemWriter<String> imprimeWrite() {
        return itens -> itens.forEach(System.out::println);
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
        taskExecutor.setConcurrencyLimit(1);
        return taskExecutor;
    }


}

