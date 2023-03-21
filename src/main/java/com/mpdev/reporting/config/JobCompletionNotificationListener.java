package com.mpdev.reporting.config;

import com.mpdev.reporting.report.outreport.OutputItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JobCompletionNotificationListener implements JobExecutionListener {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("*** Report Transformation completed");

            jdbcTemplate.query("SELECT last_name, first_name, item_type FROM report",
                    (rs, row) -> new OutputItem(
                            rs.getString(1),
                            rs.getString(2),
                            rs.getString(3))
            ).forEach(item -> log.info("Transformed item in db: {}", item));
        }
    }
}
