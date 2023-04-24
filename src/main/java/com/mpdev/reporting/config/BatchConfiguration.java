package com.mpdev.reporting.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpdev.reporting.report.inreport.InputItem;
import com.mpdev.reporting.report.outreport.OutputItem;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfiguration {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public Job importUserJob(JobRepository jobRepository,
                             JobCompletionNotificationListener listener, Step batchStep) {
        return new JobBuilder("transformReport", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(batchStep)
                .end()
                .build();
    }

    @Bean
    public Step batchStep(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager,
                      FlatFileItemReader<InputItem> reader,
                      ItemProcessor<InputItem,OutputItem> itemProcessor,
                      JdbcBatchItemWriter<OutputItem> writer) {
        return new StepBuilder("batchStep1", jobRepository)
                .<InputItem, OutputItem> chunk(10, transactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .build();
    }

    @Bean
    public FlatFileItemReader<InputItem> readerJson() {
        return new FlatFileItemReaderBuilder<InputItem>()
                .name("jsonItemReader")
                .resource(new ClassPathResource("input-data.json"))
                .lineMapper((line, lineNumber) -> objectMapper.readValue(line, InputItem.class))
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<OutputItem> writerJdbc(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<OutputItem>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO report (contract_id, last_name, first_name, item_type, jurisdiction, end_date) " +
                        "VALUES (:contractId, :lastName, :firstName, :itemType, :jurisdiction, :endDate)")
                .dataSource(dataSource)
                .build();
    }
}