package com.mpdev.reporting.config;

import com.mpdev.reporting.inreport.InputItem;
import com.mpdev.reporting.outreport.OutputItem;
import com.mpdev.reporting.processor.InputItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfiguration {

    @Bean
    public FlatFileItemReader<InputItem> reader() {
        return new FlatFileItemReaderBuilder<InputItem>()
                .name("personItemReader")
                .resource(new ClassPathResource("input-data.csv"))
                .delimited()
                .names("firstName", "lastName")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(InputItem.class);
                }})
                .build();
    }

    @Bean
    public InputItemProcessor inputItemProcessor() {
        return new InputItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<OutputItem> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<OutputItem>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO report (first_name, last_name) VALUES (:firstName, :lastName)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importUserJob(JobRepository jobRepository,
                             JobCompletionNotificationListener listener, Step step1) {
        return new JobBuilder("importUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository,
                     PlatformTransactionManager transactionManager, JdbcBatchItemWriter<OutputItem> writer) {
        return new StepBuilder("step1", jobRepository)
                .<InputItem, OutputItem> chunk(10, transactionManager)
                .reader(reader())
                .processor(inputItemProcessor())
                .writer(writer)
                .build();
    }

}