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