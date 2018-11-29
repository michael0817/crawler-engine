package com.pab.framework.crawlerengine.db;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by PrimaryKey on 17/2/4.
 */
@SuppressWarnings("AlibabaRemoveCommentedCode")
@Configuration
@MapperScan(basePackages = CrawlerDBConfig.PACKAGE, sqlSessionFactoryRef = "crawlerSqlSessionFactory")
public class CrawlerDBConfig {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    static final String PACKAGE = "com.pab.framework.crawlerdb.dao";
    static final String MAPPER_LOCATION = "classpath*:mybatis/**/*.xml";
    static final String DOMAIN_PACKAGE = "com.pab.framework.crawlerdb.domain";

    @Value("${spring.datasource.crawler.url}")
    private String dbUrl;

    @Value("${spring.datasource.crawler.username}")
    private String username;

    @Value("${spring.datasource.crawler.password}")
    private String password;

    @Value("${spring.datasource.crawler.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.crawler.initialSize}")
    private int initialSize;

    @Value("${spring.datasource.crawler.minIdle}")
    private int minIdle;

    @Value("${spring.datasource.crawler.maxActive}")
    private int maxActive;

    @Value("${spring.datasource.crawler.maxWait}")
    private int maxWait;

    @Value("${spring.datasource.crawler.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.crawler.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;

    @Value("${spring.datasource.crawler.validationQuery}")
    private String validationQuery;

    @Value("${spring.datasource.crawler.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${spring.datasource.crawler.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${spring.datasource.crawler.testOnReturn}")
    private boolean testOnReturn;

    @Value("${spring.datasource.crawler.poolPreparedStatements}")
    private boolean poolPreparedStatements;

    @Value("${spring.datasource.crawler.maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionSize;

    @Value("${spring.datasource.crawler.filters}")
    private String filters;

    @Value("{spring.datasource.crawler.connectionProperties}")
    private String connectionProperties;

    @Bean(name="crawlerDataSource")   //声明其为Bean实例
    public DataSource crawlerDataSource() {
        DruidDataSource datasource = new DruidDataSource();

        datasource.setUrl(this.dbUrl);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);

        //configuration
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        datasource.setMaxWait(maxWait);
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setValidationQuery(validationQuery);
        datasource.setTestWhileIdle(testWhileIdle);
        datasource.setTestOnBorrow(testOnBorrow);
        datasource.setTestOnReturn(testOnReturn);
        datasource.setPoolPreparedStatements(poolPreparedStatements);
        datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        try {
            datasource.setFilters(filters);
        } catch (SQLException e) {
            logger.error("druid configuration initialization filter", e);
        }
        datasource.setConnectionProperties(connectionProperties);

        return datasource;
    }

    @Bean(name = "crawlerTransactionManager")
    public DataSourceTransactionManager crawlerTransactionManager() {
        return new DataSourceTransactionManager(crawlerDataSource());
    }

    @Bean(name = "crawlerSqlSessionFactory")
    public SqlSessionFactory crawlerSqlSessionFactory(@Qualifier("crawlerDataSource") DataSource crawlerDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(crawlerDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(CrawlerDBConfig.MAPPER_LOCATION));
        sessionFactory.setTypeAliasesPackage(DOMAIN_PACKAGE);
        //mybatis 数据库字段与实体类属性驼峰映射配置
        sessionFactory.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        return sessionFactory.getObject();
    }

}

