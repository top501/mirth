ALTER TABLE CONFIGURATION ALTER COLUMN VALUE DROP NOT NULL

INSERT INTO CONFIGURATION (CATEGORY, NAME, VALUE) VALUES ('core', 'server.maxqueuesize', '0')

DROP TABLE EVENT

CREATE TABLE EVENT
	(ID INTEGER DEFAULT nextval('EVENT_SEQUENCE') NOT NULL PRIMARY KEY,
	DATE_CREATED TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	NAME TEXT NOT NULL,
	EVENT_LEVEL VARCHAR(40) NOT NULL,
	OUTCOME VARCHAR(40) NOT NULL,
	ATTRIBUTES TEXT,
	USER_ID INTEGER NOT NULL,
	IP_ADDRESS VARCHAR(40))

ALTER TABLE SCRIPT ALTER COLUMN GROUP_ID TYPE VARCHAR(40)

ALTER TABLE SCRIPT ALTER COLUMN ID TYPE VARCHAR(40)

ALTER TABLE CODE_TEMPLATE ALTER COLUMN TOOLTIP DROP NOT NULL
