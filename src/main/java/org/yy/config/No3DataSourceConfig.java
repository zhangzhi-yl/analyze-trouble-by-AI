package org.yy.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 说明：第三数据源配置
 * 作者：YY 356703572
 */
@Configuration
@MapperScan(basePackages = No3DataSourceConfig.PACKAGE, sqlSessionFactoryRef = "no3SqlSessionFactory")	//扫描 Mapper 接口并容器管理
public class No3DataSourceConfig {

    static final String PACKAGE = "org.yy.mapper.dsno3";								//master 目录
    static final String MAPPER_LOCATION = "classpath:mybatis/dsno3/*/*.xml";			//扫描的 xml 目录
    static final String CONFIG_LOCATION = "classpath:mybatis/dsno3/mybatis-config.xml"; //自定义的mybatis config 文件位置
    static final String TYPE_ALIASES_PACKAGE = "org.yy.entity"; 						//扫描的 实体类 目录
 
    @Value("${datasource.no3.url}")
    private String url;
 
    @Value("${datasource.no3.username}")
    private String user;
 
    @Value("${datasource.no3.password}")
    private String password;
 
    @Value("${datasource.no3.driver-class-name}")
    private String driverClass;
 
    @Bean(name = "no3DataSource")
    public DataSource no3DataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }
 
    @Bean(name = "no3TransactionManager")
    public DataSourceTransactionManager no3TransactionManager() {
        return new DataSourceTransactionManager(no3DataSource());
    }
 
    @Bean(name = "no3SqlSessionFactory")
    public SqlSessionFactory no3SqlSessionFactory(@Qualifier("no3DataSource") DataSource no3DataSource)throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(no3DataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(No3DataSourceConfig.MAPPER_LOCATION));
        sessionFactory.setConfigLocation(new DefaultResourceLoader().getResource(No3DataSourceConfig.CONFIG_LOCATION));
        sessionFactory.setTypeAliasesPackage(No3DataSourceConfig.TYPE_ALIASES_PACKAGE);
        return sessionFactory.getObject();
    }
}
