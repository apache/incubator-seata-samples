--
-- Licensed to the Apache Software Foundation (ASF) under one or more
-- contributor license agreements.  See the NOTICE file distributed with
-- this work for additional information regarding copyright ownership.
-- The ASF licenses this file to You under the Apache License, Version 2.0
-- (the "License"); you may not use this file except in compliance with
-- the License.  You may obtain a copy of the License at
--
--     http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

CREATE CACHED TABLE "PUBLIC"."SEATA_STATE_INST"(
    "ID" VARCHAR NOT NULL COMMENT 'id',
    "MACHINE_INST_ID" VARCHAR NOT NULL COMMENT 'state machine instance id',
    "NAME" VARCHAR NOT NULL COMMENT 'state name',
    "TYPE" VARCHAR COMMENT 'state type',
    "SERVICE_NAME" VARCHAR COMMENT 'service name',
    "SERVICE_METHOD" VARCHAR COMMENT 'method name',
    "SERVICE_TYPE" VARCHAR COMMENT 'service type',
    "BUSINESS_KEY" VARCHAR COMMENT 'business key',
    "STATE_ID_COMPENSATED_FOR" VARCHAR COMMENT 'state compensated for',
    "STATE_ID_RETRIED_FOR" VARCHAR COMMENT 'state retried for',
    "GMT_STARTED" TIMESTAMP NOT NULL COMMENT 'start time',
    "IS_FOR_UPDATE" TINYINT COMMENT 'is service for update',
    "INPUT_PARAMS" CLOB COMMENT 'input parameters',
    "OUTPUT_PARAMS" CLOB COMMENT 'output parameters',
    "STATUS" VARCHAR NOT NULL COMMENT 'status(SU succeed|FA failed|UN unknown|SK skipped|RU running)',
    "EXCEP" BLOB COMMENT 'exception',
    "GMT_UPDATED" TIMESTAMP COMMENT 'update time',
    "GMT_END" TIMESTAMP COMMENT 'end time'
);


-- PUBLIC.SEATA_STATE_MACHINE_DEF definition

CREATE CACHED TABLE "PUBLIC"."SEATA_STATE_MACHINE_DEF"(
    "ID" VARCHAR NOT NULL COMMENT 'id',
    "NAME" VARCHAR NOT NULL COMMENT 'name',
    "TENANT_ID" VARCHAR NOT NULL COMMENT 'tenant id',
    "APP_NAME" VARCHAR NOT NULL COMMENT 'application name',
    "TYPE" VARCHAR COMMENT 'state language type',
    "COMMENT_" VARCHAR COMMENT 'comment',
    "VER" VARCHAR NOT NULL COMMENT 'version',
    "GMT_CREATE" TIMESTAMP NOT NULL COMMENT 'create time',
    "STATUS" VARCHAR NOT NULL COMMENT 'status(AC:active|IN:inactive)',
    "CONTENT" CLOB COMMENT 'content',
    "RECOVER_STRATEGY" VARCHAR COMMENT 'transaction recover strategy(compensate|retry)'
);


-- PUBLIC.SEATA_STATE_MACHINE_INST definition

CREATE CACHED TABLE "PUBLIC"."SEATA_STATE_MACHINE_INST"(
    "ID" VARCHAR NOT NULL COMMENT 'id',
    "MACHINE_ID" VARCHAR NOT NULL COMMENT 'state machine definition id',
    "TENANT_ID" VARCHAR NOT NULL COMMENT 'tenant id',
    "PARENT_ID" VARCHAR COMMENT 'parent id',
    "GMT_STARTED" TIMESTAMP NOT NULL COMMENT 'start time',
    "BUSINESS_KEY" VARCHAR COMMENT 'business key',
    "START_PARAMS" CLOB COMMENT 'start parameters',
    "GMT_END" TIMESTAMP COMMENT 'end time',
    "EXCEP" BLOB COMMENT 'exception',
    "END_PARAMS" CLOB COMMENT 'end parameters',
    "STATUS" VARCHAR COMMENT 'status(SU succeed|FA failed|UN unknown|SK skipped|RU running)',
    "COMPENSATION_STATUS" VARCHAR COMMENT 'compensation status(SU succeed|FA failed|UN unknown|SK skipped|RU running)',
    "IS_RUNNING" TINYINT COMMENT 'is running(0 no|1 yes)',
    "GMT_UPDATED" TIMESTAMP NOT NULL
);