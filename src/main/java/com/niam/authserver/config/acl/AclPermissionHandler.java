package com.niam.authserver.config.acl;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.log.LogMessage;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.model.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.*;

@Log4j2
@Component
public class AclPermissionHandler {
    private final CustomizedAclService aclService;
    private final SidRetrievalStrategy sidRetrievalStrategy;
    private final PermissionEvaluator permissionEvaluator;
    private final PermissionFactory permissionFactory;

    public AclPermissionHandler(CustomizedAclService aclService, SidRetrievalStrategy sidRetrievalStrategy, PermissionEvaluator permissionEvaluator, PermissionFactory permissionFactory) {
        this.aclService = aclService;
        this.sidRetrievalStrategy = sidRetrievalStrategy;
        this.permissionEvaluator = permissionEvaluator;
        this.permissionFactory = permissionFactory;
    }

    public Set<Long> getListOfPermissionsIdByType(Authentication authentication, String type, String permission) {
        List<Sid> sids = this.sidRetrievalStrategy.getSids(authentication);
        List<Permission> requiredPermission = buildPermission(permission);
        log.debug(LogMessage.of(() -> "Checking permission '" + requiredPermission + "' for user '" + authentication.getPrincipal() + "'"));
        Set<Long> ids = new HashSet<>();
        Map<ObjectIdentity, Acl> result = this.aclService.readAclsByType(type, sids);
        result.keySet().forEach(
                o -> {
                    try {
                        Acl acl = result.get(o);
                        if (acl.isGranted(requiredPermission, sids, false)) {
                            log.debug("Access is granted");
                            ids.add(((Long) acl.getObjectIdentity().getIdentifier()));
                        }
                    } catch (NotFoundException var7) {
                        log.debug("Empty ACLs - no ACLs apply for this principal");
                    }
                }
        );
        return ids;
    }

    public Set<String> getListOfPermissionNamesByType(Authentication authentication, String type, String permission) {
        List<Sid> sids = this.sidRetrievalStrategy.getSids(authentication);
        List<Permission> requiredPermission = buildPermission(permission);
        log.debug(LogMessage.of(() -> "Checking permission '" + requiredPermission + "' for user '" + authentication.getPrincipal() + "'"));
        Set<String> names = new HashSet<>();
        Map<ObjectIdentity, Acl> result = this.aclService.readAclsByType(type, sids);
        result.keySet().forEach(
                o -> {
                    try {
                        Acl acl = result.get(o);
                        if (acl.isGranted(requiredPermission, sids, false)) {
                            log.debug("Access is granted");
                            names.add(((GrantedAuthoritySid) acl.getOwner()).getGrantedAuthority());
                        }
                    } catch (NotFoundException var7) {
                        log.debug("Empty ACLs - no ACLs apply for this principal");
                    }
                }
        );
        return names;
    }

    private List<Permission> buildPermission(String permission) {
        return Collections.singletonList(this.permissionFactory.buildFromName(permission));
    }

    public boolean hasPermission(Authentication authentication, Object targetDomainObject, String permission) {
        return permissionEvaluator.hasPermission(authentication, targetDomainObject, permission);
    }
}