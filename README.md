<!--
    Licensed to the Apache Software Foundation (ASF) under one or more
    contributor license agreements.  See the NOTICE file distributed with
    this work for additional information regarding copyright ownership.
    The ASF licenses this file to You under the Apache License, Version 2.0
    (the "License"); you may not use this file except in compliance with
    the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
-->

# samples code specification

##  Directory Structure

The first and second levels are more of a directory

Top level: seata-samples

Second layer: at-sample, tcc-sample, saga-sample, xa-sample

Third floor, The third layer is the specific sample and the naming convention is as follows:

## naming

naming with framework: spring-nacos-seata, springboot-naocs-zk-seata ...

## dependency

pom: The dependencies of each sample should be independent and should not depend on the dependencies of the parent pom of seata samples.



# samples transaction model
https://seata.apache.org/docs/user/quickstart/

## start sequence

1、account

2、storage

3、order

4、business