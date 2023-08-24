package cc.cybereflex.infrastructure.component;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Map;

public abstract class AbstractScheduleJob extends QuartzJobBean {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractScheduleJob.class);

    protected abstract Trigger trigger();

    protected abstract JobKey jobKey();

    protected abstract Map<String, Object> jobData();


    protected abstract void process(JobExecutionContext context);

    @Override
    protected void executeInternal(@NonNull JobExecutionContext context) throws JobExecutionException {
        logger.info("job:[{}] begin execute", jobKey().toString());
        execute(context);
        logger.info("job:[{}] finshed execute", jobKey().toString());
    }
}
