package com.niam.authserver.config.acl;

import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.acls.model.SidRetrievalStrategy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class CustomizedSidRetrievalStrategyImpl implements SidRetrievalStrategy {
    @Override
    public List<Sid> getSids(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<Sid> sids = new ArrayList<>(authorities.size());
        for (GrantedAuthority authority : authorities) {
            sids.add(new GrantedAuthoritySid(authority));
        }
        return sids;
    }
}
