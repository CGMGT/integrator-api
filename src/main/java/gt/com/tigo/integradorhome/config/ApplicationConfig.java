package gt.com.tigo.integradorhome.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "gt.com.tigo.integradorhome.dao",
        entityManagerFactoryRef = "applicationEntityManager",
        transactionManagerRef = "applicationTransactionManager"
)
public class ApplicationConfig {

    @Value("${spring.datasource.application.jndi-name}")
    private String applicationJndi;

    @Bean(destroyMethod = "")
    @Primary
    public DataSource applicationDataSource() {
        JndiDataSourceLookup lookup = new JndiDataSourceLookup();
        return lookup.getDataSource(this.applicationJndi);
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean applicationEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        em.setDataSource(applicationDataSource());

        em.setPackagesToScan("gt.com.tigo.integradorhome.model");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        return em;
    }

    @Bean
    @Primary
    public PlatformTransactionManager applicationTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();

        transactionManager.setEntityManagerFactory(applicationEntityManager().getObject());

        return transactionManager;
    }


}
