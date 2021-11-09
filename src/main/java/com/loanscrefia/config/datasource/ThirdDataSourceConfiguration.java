package com.loanscrefia.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(value = "com.loanscrefia.common.common.sms.repository", sqlSessionFactoryRef = "thirdSqlSessionFactory")
public class ThirdDataSourceConfiguration {
    @Bean(name = "thirdDataSource")
    @ConfigurationProperties(prefix="spring.datasource.hikari.sms")
    public DataSource thirdDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "thirdSqlSessionFactory")
    public SqlSessionFactory thirdSqlSessionFactory(@Qualifier("thirdDataSource") DataSource thirdDataSource,
                                                          ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        
        //sqlSessionFactoryBean.setEnvironment("mssql");
        
        sqlSessionFactoryBean.setDataSource(thirdDataSource);
        sqlSessionFactoryBean.setVfs(SpringBootVFS.class);  // Spring Boot 전용 VFS 사용하도록 지정
        sqlSessionFactoryBean.setTypeAliasesPackage("com.loanscrefia.*.*.*.domain");
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/sms/**.xml"));
        org.apache.ibatis.session.Configuration ibatisConfiguration = new org.apache.ibatis.session.Configuration();
        ibatisConfiguration.setMapUnderscoreToCamelCase(true);
        sqlSessionFactoryBean.setConfiguration(ibatisConfiguration);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "thirdSessionTemplate")
    public SqlSessionTemplate thirdSqlSessionTemplate(@Qualifier("thirdSqlSessionFactory") SqlSessionFactory thirdSqlSessionFactory) {
        return new SqlSessionTemplate(thirdSqlSessionFactory);
    }
}