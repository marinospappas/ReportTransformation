package com.mpdev.reporting.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpdev.reporting.report.inreport.InputItem;
import com.mpdev.reporting.report.outreport.OutputItem;
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

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final InputItemProcessor inputItemProcessor;

    public BatchConfiguration(InputItemProcessor inputItemProcessor) {
        this.inputItemProcessor = inputItemProcessor;
    }

    @Bean
    public FlatFileItemReader<InputItem> readerCsv() {
        return new FlatFileItemReaderBuilder<InputItem>()
                .name("csvItemReader")
                .resource(new ClassPathResource("input-data.csv"))
                .delimited()
                .names("firstName", "lastName")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(InputItem.class);
                }})
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
    public JdbcBatchItemWriter<OutputItem> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<OutputItem>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO report (contract_id, last_name, first_name, item_type) VALUES (:contractId, :lastName, :firstName, :itemType)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importUserJob(JobRepository jobRepository,
                             JobCompletionNotificationListener listener, Step step1) {
        return new JobBuilder("transformReport", jobRepository)
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
                .reader(readerJson())
                .processor(inputItemProcessor)
                .writer(writer)
                .build();
    }

}