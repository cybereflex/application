package cc.cybereflex.infrastructure.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;

public abstract class AbstractAppInitial implements CommandLineRunner {
    protected final static Logger logger = LoggerFactory.getLogger(AbstractAppInitial.class);

    @Override
    public void run(String... args) throws Exception {
        init(args);
    }

    protected abstract void init(String... args);
}
