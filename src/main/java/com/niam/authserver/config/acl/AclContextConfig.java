package com.niam.authserver.config.acl;

import com.niam.authserver.utils.SmsPanelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionCacheOptimizer;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.acls.model.SidRetrievalStrategy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.sql.DataSource;

@EnableCaching
@RequiredArgsConstructor
@Configuration
public class AclContextConfig {
    private final CacheManager cacheManager;
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public AclCache aclCache(final PermissionGrantingStrategy permissionGrantingStrategy,
                             final AclAuthorizationStrategy aclAuthorizationStrategy) {
        return new SpringCacheBasedAclCache(cacheManager.getCache(SmsPanelRepository.ACL_CACHE), permissionGrantingStrategy, aclAuthorizationStrategy);
    }

    @Bean
    public PermissionGrantingStrategy permissionGrantingStrategy() {
        return new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
    }

    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Bean
    public MethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler(final AclService aclService,
                                                                                  final PermissionEvaluator permissionEvaluator) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(permissionEvaluator);
        expressionHandler.setPermissionCacheOptimizer(new AclPermissionCacheOptimizer(aclService));
        return expressionHandler;
    }

    @Bean
    public PermissionEvaluator permissionEvaluator(final AclService aclService) {
        return new AclPermissionEvaluator(aclService);
    }

    @Bean
    public LookupStrategy lookupStrategy(final AclAuthorizationStrategy aclAuthorizationStrategy, final AclCache aclCache) {
        return new BasicLookupStrategy(dataSource(), aclCache, aclAuthorizationStrategy, new ConsoleAuditLogger());
    }

    @Bean
    public AclService aclService(final LookupStrategy lookupStrategy, final AclCache aclCache) {
        return new JdbcMutableAclService(dataSource(), lookupStrategy, aclCache);
    }

    @Bean
    public SidRetrievalStrategy sidRetrievalStrategy() {
        return new SidRetrievalStrategyImpl();
    }

    @Bean
    public CustomizedAclService customizedAclService(final DataSource dataSource, final LookupStrategy lookupStrategy) {
        return new CustomizedAclService(dataSource, lookupStrategy);
    }

    @Bean
    public PermissionFactory permissionFactory() {
        return new DefaultPermissionFactory();
    }
}