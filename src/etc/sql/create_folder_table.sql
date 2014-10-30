CREATE TABLE %TABLE_NAME% (
	domain VARCHAR(100),
	fileName VARCHAR(255) NOT NULL,
	fileNameLower VARCHAR(255) NOT NULL,
	dir BOOLEAN NOT NULL,
	size BIGINT NOT NULL,
	modifiedByNodeId VARCHAR(100),
	lastModifiedDate BIGINT NOT NULL,
	version BIGINT NOT NULL,
	deleted BOOLEAN NOT NULL,
	folderId VARCHAR(100),
);
CREATE INDEX IDX_%TABLE_NAME%_FN ON %TABLE_NAME%(fileName);
CREATE INDEX IDX_%TABLE_NAME%_FNL ON %TABLE_NAME%(fileNameLower);
