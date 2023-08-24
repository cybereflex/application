package cc.cybereflex.resources.component;

import cc.cybereflex.infrastructure.SpringContextHolder;
import cc.cybereflex.infrastructure.component.AbstractAppInitial;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;

@Component
public class DatabaseInitial extends AbstractAppInitial {

    @Override
    protected void init(String... args) {
        try {
            logger.info("begin init database...");
            HikariDataSource hikariDataSource = SpringContextHolder.getBean(HikariDataSource.class);

            ClassPathResource classPathResource = new ClassPathResource("init/database.sql");

            ScriptRunner scriptRunner = new ScriptRunner(hikariDataSource.getConnection());
            scriptRunner.setAutoCommit(false);
            scriptRunner.setStopOnError(false);
            scriptRunner.setEscapeProcessing(true);
            scriptRunner.runScript(new InputStreamReader(classPathResource.getInputStream()));

            logger.info("init database success");
        } catch (Exception e) {
            logger.error("init database failed", e);
        }
    }
}
