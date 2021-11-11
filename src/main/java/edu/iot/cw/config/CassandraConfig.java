package edu.iot.cw.config;

import edu.iot.cw.repositories.MeasurementRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableCassandraRepositories(basePackageClasses = MeasurementRepository.class)
public class CassandraConfig extends AbstractCassandraConfiguration {
    @Value("${username}")
    protected String username;

    @Value("${password}")
    protected String password;

    @Value("${keyspace-name}")
    protected String keyspace;

    @Value("${port}")
    protected int port;

    @Value("${contact-points}")
    protected String contactPoints;

    @Override
    protected String getKeyspaceName() {
        return keyspace;
    }

    @Override
    protected String getLocalDataCenter() {
        return "DC1";
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[]{"edu.iot.cw.data.model"};
    }

}
