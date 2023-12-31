--DROP TABLE GIN_DISPATCH_ATTACHMENTS;
CREATE TABLE TQ_GIS.GIN_DISPATCH_ATTACHMENTS
(
  DA_CODE               NUMBER(22)          NOT NULL,
  DA_ALERT_CODE         NUMBER(22)          NOT NULL,
  DA_DISPATCH_DOC_CODE  NUMBER(22)          NOT NULL,
  DA_SHT_DESC           VARCHAR2(15 BYTE)   NOT NULL,
  DA_DESC               VARCHAR2(255 BYTE)  NOT NULL
);


CREATE UNIQUE INDEX TQ_GIS.GIN_DSPTCH_ATTCHMNTS_PK ON TQ_GIS.GIN_DISPATCH_ATTACHMENTS
(DA_CODE);

--DROP PUBLIC SYNONYM GIN_DISPATCH_ATTACHMENTS;

CREATE PUBLIC SYNONYM GIN_DISPATCH_ATTACHMENTS FOR TQ_GIS.GIN_DISPATCH_ATTACHMENTS;

ALTER TABLE TQ_GIS.GIN_DISPATCH_ATTACHMENTS ADD (
  CONSTRAINT GIN_DSPTCH_ATTCHMNTS_PK
 PRIMARY KEY
 (DA_CODE));

GRANT ALTER, DELETE, INSERT, SELECT, UPDATE, ON COMMIT REFRESH, QUERY REWRITE, DEBUG, FLASHBACK ON TQ_GIS.GIN_DISPATCH_ATTACHMENTS TO CONNECT;

GRANT ALTER, DELETE, INSERT, SELECT, UPDATE, ON COMMIT REFRESH, QUERY REWRITE, DEBUG, FLASHBACK ON TQ_GIS.GIN_DISPATCH_ATTACHMENTS TO DBA;

GRANT ALTER, DELETE, INDEX, INSERT, REFERENCES, SELECT, UPDATE, ON COMMIT REFRESH, QUERY REWRITE, DEBUG, FLASHBACK ON TQ_GIS.GIN_DISPATCH_ATTACHMENTS TO PUBLIC;

GRANT SELECT ON TQ_GIS.GIN_DISPATCH_ATTACHMENTS TO QUERYING_ROLE;

GRANT ALTER, DELETE, INSERT, SELECT, UPDATE, ON COMMIT REFRESH, QUERY REWRITE, DEBUG, FLASHBACK ON TQ_GIS.GIN_DISPATCH_ATTACHMENTS TO RESOURCE;






