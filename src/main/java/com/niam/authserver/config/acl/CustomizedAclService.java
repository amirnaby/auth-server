package com.niam.authserver.config.acl;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.jdbc.JdbcAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomizedAclService extends JdbcAclService {
    private final static String FIND_ALL_ACL_OBJECT_IDENTITY_BY_TYPE_AND_SID =
            "select a.id from acl_object_identity a" +
            "    where a.object_id_class = (select b.id from acl_class b where b.class=?)" +
            "      and a.owner_sid in (" +
            "        select c.id from acl_sid c where c.sid in ?" +
            "    )";
    private static final String FIND_ALL_OBJECT_IDENTITY_ID_BY_TYPE =
            "select distinct a.object_id_identity from acl_object_identity a" +
            "    where a.object_id_class = (select b.id from acl_class b where b.class=?)";
    private LookupStrategy lookupStrategy;

    public CustomizedAclService(DataSource dataSource, LookupStrategy lookupStrategy) {
        super(dataSource, lookupStrategy);
        this.lookupStrategy = lookupStrategy;
    }

    public CustomizedAclService(JdbcOperations jdbcOperations, LookupStrategy lookupStrategy) {
        super(jdbcOperations, lookupStrategy);
    }

    public List<Long> findAllAclObjectIdentityByTypeAndSids(String type, String[] sids) {
        return super.jdbcOperations.queryForList(FIND_ALL_ACL_OBJECT_IDENTITY_BY_TYPE_AND_SID, Long.class, type, sids);
    }

    public List<ObjectIdentity> findAllObjectIdentityByType(String type) {
        List<Long> ids = super.jdbcOperations.queryForList(FIND_ALL_OBJECT_IDENTITY_ID_BY_TYPE, Long.class, type);
        return ids.stream().map(i -> new ObjectIdentityImpl(type, i)).collect(Collectors.toList());
    }

    public Map<ObjectIdentity, Acl> readAclsByType(String type, List<Sid> sids) {
        List<ObjectIdentity> objectIdentities = findAllObjectIdentityByType(type);
        if (!objectIdentities.isEmpty())
            return super.readAclsById(objectIdentities, sids);
        return Collections.emptyMap();
    }

//    @Override
//    public Map<ObjectIdentity, Acl> readAclsById(List<ObjectIdentity> objects, List<Sid> sids) throws NotFoundException {
//        Map<ObjectIdentity, Acl> map = this.lookupStrategy.readAclsById(objects, sids);
//        objects.removeIf(objectIdentity -> !map.containsKey(objectIdentity));
//        return map;
//    }
}