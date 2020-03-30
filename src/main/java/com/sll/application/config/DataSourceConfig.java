package com.sll.application.config;


import com.alibaba.druid.pool.DruidDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Demo class
 *
 * @author shill12
 * @date 14:47 2019/11/11
 */
@Configuration
public class DataSourceConfig {

    @Value("${metouch.datasource.driver-class-name}")
    private String driverClassName;
    @Value("${metouch.datasource.url}")
    private String jdbcUrl;
    @Value("${metouch.datasource.username}")
    private String userName;

    @Value("${metouch.datasource.databasename}")
    private String databasename;

    @Value("${metouch.datasource.enc-text}")
    private String rdsEncText;

    @Value("${spring.profiles.active}")
    private String active;

    @Bean
    public DataSource dataSource() {

        //System.setProperty("javax.net.ssl.trustStore", "证书文件路径");
        //System.setProperty("javax.net.ssl.trustStorePassword", "证书密码");
        //jdbcUrl中需要修为： jdbc:mysql://mysql-001-aliyun.leopaard-dms-dev:31301/metouch?useUnicode=true&characterEncoding=utf8&verifyServerCertificate=true&useSSL=true&requireSSL=true
        //HikariDataSource dataSource = new HikariDataSource();
        //dataSource.setDriverClassName(driverClassName);
        //dataSource.setJdbcUrl(jdbcUrl);
        //dataSource.setUsername(userName);
        //dataSource.setPassword(SansecUtil.getDataSourcePassowrd(rdsCertPath, rdsEncryptDataKey, rdsEncText));


        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName(jdbcUrl);
        dataSource.setPort(3306);
        dataSource.setUser("root");
        dataSource.setPassword("000000");
        dataSource.setDatabaseName(databasename);
        try {
            dataSource.setCharacterEncoding("UTF-8");
            if(!active.equals("local")){
                dataSource.setLogger("com.mysql.cj.log.StandardLogger");
                dataSource.setUseSSL(true);
                dataSource.setRequireSSL(true);
                dataSource.setVerifyServerCertificate(true);
                dataSource.setTrustCertificateKeyStoreType("JKS");
                dataSource.setTrustCertificateKeyStoreUrl("file:/app/keystore.jks");
                dataSource.setTrustCertificateKeyStorePassword("000000");
            }else {
                dataSource.setServerTimezone("GMT+8");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataSource;
    }




    //@Bean
    public DataSource getDruidDataSource() {
            DruidDataSource dataSource = new DruidDataSource();
            /*  //设置mysql-ssl证书
            System.setProperty("javax.net.ssl.trustStore","/app/keystore.jks");
            System.setProperty("javax.net.ssl.trustStorePassword", "000000");*/
            dataSource.setDriverClassName(driverClassName);
            dataSource.setUrl("jdbc:mysql://localhost:3306/metouch?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8");
            dataSource.setUsername("root");
            dataSource.setPassword("000000");
            return dataSource;
    }

}