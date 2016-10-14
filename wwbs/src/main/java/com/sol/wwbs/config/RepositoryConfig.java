package com.sol.wwbs.config;

import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan("com.mom.watch")
@PropertySource("classpath:config.properties")
@EnableJpaRepositories("com.mom.watch")
public class RepositoryConfig {
    @Resource
    private Environment env;

	
	@Bean(destroyMethod="close")
	public DataSource dataSource() {
		
		System.out.println("path : " + System.getProperty("user.dir"));
		System.out.println("path : " + System.getProperty("app.path"));
		System.out.println("path : " + env.getProperty("app.path"));
		
		BasicDataSource dataSource = new BasicDataSource();
				
		dataSource.setDriverClassName(env.getProperty("database.jdbcClass"));
		dataSource.setUrl(env.getProperty("database.jdbcUrl"));
		dataSource.setUsername(env.getProperty("database.dbUser"));
		dataSource.setPassword(env.getProperty("database.dbPassword"));
		
		// optional
		dataSource.setValidationQuery(env.getProperty("database.validationQuery","SELECT 1"));		
		dataSource.setInitialSize(env.getProperty("database.initialSize",Integer.class,5));
		dataSource.setMaxActive(env.getProperty("database.maxActive",Integer.class,30));
		dataSource.setMaxIdle(env.getProperty("database.MaxIdle",Integer.class,5));
		dataSource.setMaxWait(env.getProperty("database.maxWait",Integer.class,30000));
		dataSource.setDefaultAutoCommit(true);
				
		System.out.println("Config : dataSource");
		return dataSource; 
	}
	
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        entityManagerFactoryBean.setPackagesToScan(getPackagesToScan());
                 
		Properties properties = new Properties(); 
		
		properties.setProperty("dialect",env.getProperty("hibernate.dialect")); 
        properties.setProperty("show_sql", env.getProperty("hibernate.show_sql")); 
        properties.setProperty("hbm2ddl.auto", env.getProperty("hibernate.hdm2ddl")); 
        
        entityManagerFactoryBean.setJpaProperties(properties);
       
        System.out.println("Config : entityManagerFactory"); 
        return entityManagerFactoryBean;
    }
    
	@Bean
	public JpaVendorAdapter jpaVendorAdapter(){
	    HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
	    jpaVendorAdapter.setGenerateDdl(true);
	    jpaVendorAdapter.setShowSql(true);

	    return jpaVendorAdapter;
	}
	
    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        
        System.out.println("Config : transactionManager"); 
        return transactionManager;
    }
	
	
	protected String[] getPackagesToScan() { 
		return new String[]{"com.sol.wwbs"}; 
	} 
}
