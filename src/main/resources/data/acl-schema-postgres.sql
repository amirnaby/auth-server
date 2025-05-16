CREATE TABLE acl_sid
(
    id        BIGINT PRIMARY KEY,
    principal BOOLEAN      NOT NULL,
    sid       VARCHAR(100) NOT NULL,
    CONSTRAINT unique_acl_sid UNIQUE (sid, principal)
);

CREATE SEQUENCE acl_sid_sequence START 1 INCREMENT 1 NO MAXVALUE;

CREATE OR REPLACE FUNCTION acl_sid_id_trigger()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.id := NEXTVAL('acl_sid_sequence');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER acl_sid_id_trigger
    BEFORE INSERT
    ON acl_sid
    FOR EACH ROW
EXECUTE FUNCTION acl_sid_id_trigger();

----------------------------------------------------------------------------

CREATE TABLE acl_class
(
    id    BIGINT PRIMARY KEY,
    class VARCHAR(100) NOT NULL,
    CONSTRAINT uk_acl_class UNIQUE (class)
);

CREATE SEQUENCE acl_class_sequence START 1 INCREMENT 1 NO MAXVALUE;

CREATE OR REPLACE FUNCTION acl_class_id_trigger()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.id := NEXTVAL('acl_class_sequence');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER acl_class_id_trigger
    BEFORE INSERT
    ON acl_class
    FOR EACH ROW
EXECUTE FUNCTION acl_class_id_trigger();

------------------------------------------------------------------------------------------------

CREATE TABLE acl_object_identity
(
    id                 BIGINT PRIMARY KEY,
    object_id_class    BIGINT  NOT NULL,
    object_id_identity BIGINT  NOT NULL,
    parent_object      BIGINT,
    owner_sid          BIGINT,
    entries_inheriting BOOLEAN NOT NULL,
    CONSTRAINT uk_acl_object_identity UNIQUE (object_id_class, object_id_identity),
    CONSTRAINT fk_acl_object_identity_parent FOREIGN KEY (parent_object) REFERENCES acl_object_identity (id),
    CONSTRAINT fk_acl_object_identity_class FOREIGN KEY (object_id_class) REFERENCES acl_class (id),
    CONSTRAINT fk_acl_object_identity_owner FOREIGN KEY (owner_sid) REFERENCES acl_sid (id)
);

CREATE SEQUENCE acl_object_identity_seq START 1 INCREMENT 1 NO MAXVALUE;

CREATE OR REPLACE FUNCTION acl_object_identity_id_trigger()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.id := NEXTVAL('acl_object_identity_seq');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER acl_object_identity_id_trigger
    BEFORE INSERT
    ON acl_object_identity
    FOR EACH ROW
EXECUTE FUNCTION acl_object_identity_id_trigger();

-----------------------------------------------------------------------------------------------------------

CREATE TABLE acl_entry
(
    id                  BIGINT PRIMARY KEY,
    acl_object_identity BIGINT  NOT NULL,
    ace_order           INTEGER NOT NULL,
    sid                 BIGINT  NOT NULL,
    mask                INTEGER NOT NULL,
    granting            BOOLEAN NOT NULL,
    audit_success       BOOLEAN NOT NULL,
    audit_failure       BOOLEAN NOT NULL,
    CONSTRAINT unique_acl_entry UNIQUE (acl_object_identity, ace_order),
    CONSTRAINT fk_acl_entry_object FOREIGN KEY (acl_object_identity) REFERENCES acl_object_identity (id),
    CONSTRAINT fk_acl_entry_acl FOREIGN KEY (sid) REFERENCES acl_sid (id)
);

CREATE SEQUENCE acl_entry_sequence START 1 INCREMENT 1 NO MAXVALUE;

CREATE OR REPLACE FUNCTION acl_entry_id_trigger()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.id := NEXTVAL('acl_entry_sequence');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER acl_entry_id_trigger
    BEFORE INSERT
    ON acl_entry
    FOR EACH ROW
EXECUTE FUNCTION acl_entry_id_trigger();