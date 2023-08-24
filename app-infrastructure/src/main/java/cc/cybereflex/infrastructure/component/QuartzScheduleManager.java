package cc.cybereflex.infrastructure.component;

import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.quartz.core.jmx.JobDataMapSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class QuartzScheduleManager {

    private static final Logger logger = LoggerFactory.getLogger(QuartzScheduleManager.class);

    private final Scheduler scheduler;

    public void register(AbstractScheduleJob job) {
        try {
            JobDetail jobDetail = JobBuilder.newJob(job.getClass())
                    .withIdentity(job.jobKey())
                    .setJobData(JobDataMapSupport.newJobDataMap(job.jobData()))
                    .build();

            Trigger trigger = job.trigger();

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            logger.error("register job failed", e);
            throw new RuntimeException(e);
        }
    }

    public boolean remove(JobKey jobKey) {
        try {
            return scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            logger.error("remove job failed", e);
            throw new RuntimeException(e);
        }
    }
}
