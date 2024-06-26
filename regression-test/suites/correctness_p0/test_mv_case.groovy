// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

suite("test_mv_case") {
    sql """drop table if exists test_table_aaa2;"""
    sql """CREATE TABLE `test_table_aaa2` (
            `ordernum` varchar(65533) NOT NULL ,
            `dnt` datetime NOT NULL ,
            `data` json NULL 
            ) ENGINE=OLAP
            DUPLICATE KEY(`ordernum`, `dnt`)
            COMMENT 'OLAP'
            DISTRIBUTED BY HASH(`ordernum`) BUCKETS 3
            PROPERTIES (
            "replication_allocation" = "tag.location.default: 1"
            );"""
    sql """DROP MATERIALIZED VIEW IF EXISTS ods_zn_dnt_max1 ON test_table_aaa2;"""
    sql """create materialized view ods_zn_dnt_max1 as
            select ordernum,max(dnt) as dnt from test_table_aaa2
            group by ordernum
            ORDER BY ordernum;"""
    sql """insert into test_table_aaa2 select 'cib2205045_1_1s','2023/6/10 3:55:33','{"DB1":168939,"DNT":"2023-06-10 03:55:33"}' ;"""
    sql """insert into test_table_aaa2 select 'cib2205045_1_1s','2023/6/10 3:56:33','{"DB1":168939,"DNT":"2023-06-10 03:56:33"}' ;"""
    sql """insert into test_table_aaa2 select 'cib2205045_1_1s','2023/6/10 3:57:33','{"DB1":168939,"DNT":"2023-06-10 03:57:33"}' ;"""
    sql """insert into test_table_aaa2 select 'cib2205045_1_1s','2023/6/10 3:58:33','{"DB1":168939,"DNT":"2023-06-10 03:58:33"}' ;"""
    qt_select_default """ select * from test_table_aaa2 order by dnt;"""
}
