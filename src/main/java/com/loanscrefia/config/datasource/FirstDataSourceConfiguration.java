package com.loanscrefia.config.datasource;

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
import org.springframework.context.annotation.Primary;

@Configuration
@MapperScan(value = "com.loanscrefia.*.*.repository", sqlSessionFactoryRef = "firstSqlSessionFactory")
public class FirstDataSourceConfiguration {
     @Primary    
     @Bean(name = "firstDataSource")
     @ConfigurationProperties(prefix = "spring.datasource.hikari.mst")
     public DataSource firstDataSource() {
         return DataSourceBuilder.create().build();
     }

     @Primary
     @Bean(name = "firstSqlSessionFactory")
     public SqlSessionFactory firstSqlSessionFactory(@Qualifier("firstDataSource") DataSource firstDataSource,
                                 ApplicationContext applicationContext) throws Exception {
            SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
            sqlSessionFactoryBean.setDataSource(firstDataSource);
            sqlSessionFactoryBean.setVfs(SpringBootVFS.class);  // Spring Boot 전용 VFS 사용하도록 지정
            sqlSessionFactoryBean.setTypeAliasesPackage("com.loanscrefia.*.*.domain");
            sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/*/*/**.xml"));
            org.apache.ibatis.session.Configuration ibatisConfiguration = new org.apache.ibatis.session.Configuration();
            ibatisConfiguration.setMapUnderscoreToCamelCase(true);
            sqlSessionFactoryBean.setConfiguration(ibatisConfiguration);
            return sqlSessionFactoryBean.getObject();
     }

     @Primary
     @Bean(name = "firstSessionTemplate")
     public SqlSessionTemplate firstSqlSessionTemplate(@Qualifier("firstSqlSessionFactory") SqlSessionFactory firstSqlSessionFactory) {
         return new SqlSessionTemplate(firstSqlSessionFactory);
     }
}