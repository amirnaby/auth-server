INSERT INTO acl_sid (id, principal, sid) VALUES (1, false, 'ROLE_ADMIN');
INSERT INTO acl_sid (id, principal, sid) VALUES (4, false, 'CAMPAIN');
INSERT INTO acl_sid (id, principal, sid) VALUES (5, false, 'USSD');

INSERT INTO acl_class (id, class) VALUES (1, 'com.niam.authserver.persistence.model.BusinessSystem');

INSERT INTO permissible (id, dtype) VALUES (2, 'BusinessSystem');
INSERT INTO permissible (id, dtype) VALUES (3, 'BusinessSystem');
INSERT INTO permissible (id, dtype) VALUES (4, 'BusinessSystem');
INSERT INTO permissible (id, dtype) VALUES (5, 'BusinessSystem');

INSERT INTO BUSINESS_SYSTEM (ID, DESCRIPTION, NAME) VALUES (2, null, 'CRM');
INSERT INTO BUSINESS_SYSTEM (ID, DESCRIPTION, NAME) VALUES (3, null, 'CBS');
INSERT INTO BUSINESS_SYSTEM (ID, DESCRIPTION, NAME) VALUES (4, null, 'CAMPAIN');
INSERT INTO BUSINESS_SYSTEM (ID, DESCRIPTION, NAME) VALUES (5, null, 'USSD');

--                                    -- acl_class    -- permissible    parentPrivilege   role
INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (1, 1, 4, NULL, 4, false);

--                                                     -- Role -Read
INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (1, 1, 1, 4, 1, 1, 1, 1);