package org.apache.seata.model;

/**
 * @author jingliu_xiong@foxmail.com
 */
public class Module {
    private String name;
    private String dir;
    private String invoke;
    private String port;
    private String expect;
    private DockerService docker_service;

    public String getExpect() {
        return expect;
    }

    public void setExpect(String expect) {
        this.expect = expect;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DockerService getDocker_service() {
        return docker_service;
    }

    public void setDocker_service(DockerService docker_service) {
        this.docker_service = docker_service;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getInvoke() {
        return invoke;
    }

    public void setInvoke(String invoke) {
        this.invoke = invoke;
    }
}
