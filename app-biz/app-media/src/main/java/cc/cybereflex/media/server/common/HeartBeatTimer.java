package cc.cybereflex.media.server.common;

import cc.cybereflex.infrastructure.component.AbstractScheduleJob;
import lombok.RequiredArgsConstructor;
import org.quartz.*;

import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
public class HeartBeatTimer extends AbstractScheduleJob {


    private final Integer interval;

    @Override
    public Trigger trigger() {
        return TriggerBuilder.newTrigger()
                .startNow()
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(interval))
                .build();
    }

    @Override
    public JobKey jobKey() {
        return JobKey.jobKey("SipHeartBeat", "Media:Sip");
    }

    @Override
    public Map<String, Object> jobData() {
        return Collections.emptyMap();
    }

    @Override
    protected void process(JobExecutionContext context) {

    }
}
