package com.winflow.flowcore.util;

import org.quartz.CronExpression;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

@Component
public class CronUtil {

    public Date getNextSchedulingTime(String cron) throws ParseException {
        CronExpression cronExpression = new CronExpression(cron);

        Date now = new Date();
        return cronExpression.getNextValidTimeAfter(now);
    }
}
