INSERT INTO acl_sid (id, principal, sid)
VALUES (1, 0, 'ROLE_ADMIN');
INSERT INTO acl_sid (id, principal, sid)
VALUES (4, 0, 'CAMPAIN');
INSERT INTO acl_sid (id, principal, sid)
VALUES (5, 0, 'USSD');

INSERT INTO acl_class (id, class)
VALUES (1, 'com.niam.authserver.persistence.model.BusinessSystem');

--                                    -- acl_class    -- permissible    parentPrivilege   role
INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
VALUES (1, 1, 1, NULL, 1, 0);
INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
VALUES (2, 1, 2, NULL, 2, 0);
INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
VALUES (3, 1, 3, NULL, 3, 0);
INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
VALUES (4, 1, 4, NULL, 4, 0);
INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
VALUES (5, 1, 5, NULL, 5, 0);
INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
VALUES (6, 1, 6, NULL, 6, 0);
INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
VALUES (7, 1, 7, NULL, 7, 0);

--                                                     -- Role -Read
INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
VALUES (1, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
VALUES (2, 2, 1, 2, 1, 1, 1, 1);
INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
VALUES (3, 3, 1, 3, 1, 1, 1, 1);
INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
VALUES (4, 4, 1, 4, 1, 1, 1, 1);
INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
VALUES (5, 5, 1, 5, 1, 1, 1, 1);
INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
VALUES (6, 6, 1, 6, 1, 1, 1, 1);
INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
VALUES (7, 7, 1, 7, 1, 1, 1, 1);