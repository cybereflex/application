package cc.cybereflex.infrastructure.model.constants;

public class AutoconfigureConstants {


    //config prefix
    public static final String PREFIX_DATABASE = "app.data.database.";
    public static final String PREFIX_CACHE_REDIS = "app.data.cache.redis.";
    public static final String PREFIX_MESSAGE_RABBIT = "app.message.rabbit.";
    public static final String PREFIX_MESSAGE_MQTT = "app.message.mqtt.";
    public static final String PREFIX_SCHEDULE_QUARTZ = "app.schedule.quartz.";

    // redis cache bean name
    public static final String REDIS_CACHE_BEAN_NAME = "redisCache";


    // rabbitmq bean name
    public static final String RABBIT_CONNECTION_FACTORY_BEAN_NAME = "rabbitConnectionFactory";
    public static final String RABBIT_TEMPLATE_BEAN_NAME = "rabbitTemplate";
    public static final String RABBIT_ADMIN_BEAN_NAME = "rabbitAdmin";

    // mqtt bean name
    public static final String MQTT_INBOUND_FLOW_BEAN_NAME = "mqttInboundFlow-%d";
    public static final String MQTT_OUTBOUND_FLOW_BEAN_NAME = "mqttOutboundFlow";
    public static final String MQTT_TEMPLATE_BEAN_NAME = "mqttTemplate";

    //quartz bean name
    public static final String QUARTZ_SCHEDULE_BEAN_NAME = "schedulerBean";
    public static final String QUARTZ_SCHEDULE_MANAGER_BEAN_NAME = "quartzScheduleManager";
}
