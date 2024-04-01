version: "3.9"
services:
<#list modules.consumers as service>
    ${service.name}:
    <#if service.docker_service.image?has_content>
        image: ${service.docker_service.image}
    </#if>
    <#if service.docker_service.networks?has_content>
        networks: ${service.docker_service.networks}
    </#if>
    <#if service.docker_service.network_mode?has_content>
        network_mode: ${service.docker_service.network_mode}
    </#if>
    <#if service.docker_service.hostname?has_content>
        hostname: ${service.docker_service.hostname}
    </#if>
    <#if service.docker_service.restart??>
        restart: ${service.docker_service.restart}
    </#if>
    <#if service.docker_service.build?has_content>
        build: ${service.docker_service.build}
    </#if>
    <#if service.docker_service.container_name?has_content>
        container_name: ${service.docker_service.container_name}
    </#if>
    <#if service.docker_service.volumes??>
        volumes:
        <#list service.docker_service.volumes as volume>
            - ${volume}
        </#list>
    </#if>
    <#if service.docker_service.environment??>
        environment:
        <#list service.docker_service.environment as key,value>
            ${key}: ${value}
        </#list>
    </#if>
    <#if service.docker_service.ports??>
        ports:
        <#list service.docker_service.ports as port>
            - ${port}
        </#list>
    </#if>
    <#if service.docker_service.depends_on??>
        depends_on:
        <#list service.docker_service.depends_on?keys as key>
            ${key}:
                condition: ${service.docker_service.depends_on[key].condition}
        </#list>
    </#if>
    <#if service.docker_service.healthcheck??>
        healthcheck:
        <#list service.docker_service.healthcheck as key,value>
            ${key}: ${value}
        </#list>
    </#if>
</#list>
<#list modules.providers as service>
    ${service.name}:
        <#if service.docker_service.image?has_content>
        image: ${service.docker_service.image}
        </#if>
        <#if service.docker_service.networks?has_content>
        networks: ${service.docker_service.networks}
        </#if>
        <#if service.docker_service.network_mode?has_content>
        network_mode: ${service.docker_service.network_mode}
        </#if>
        <#if service.docker_service.hostname?has_content>
        hostname: ${service.docker_service.hostname}
        </#if>
        <#if service.docker_service.restart??>
        restart: ${service.docker_service.restart}
        </#if>
        <#if service.docker_service.build?has_content>
        build: ${service.docker_service.build}
        </#if>
        <#if service.docker_service.container_name?has_content>
        container_name: ${service.docker_service.container_name}
        </#if>
        <#if service.docker_service.volumes??>
        volumes:
        <#list service.docker_service.volumes as volume>
            - ${volume}
        </#list>
        </#if>
        <#if service.docker_service.environment??>
        environment:
        <#list service.docker_service.environment as key,value>
            ${key}: ${value}
        </#list>
        </#if>
        <#if service.docker_service.ports??>
        ports:
        <#list service.docker_service.ports as port>
            - ${port}
        </#list>
        </#if>
        <#if service.docker_service.depends_on??>
        depends_on:
        <#list service.docker_service.depends_on?keys as key>
            ${key}:
                condition: ${service.docker_service.depends_on[key].condition}
        </#list>
        </#if>
        <#if service.docker_service.healthcheck??>
        healthcheck:
        <#list service.docker_service.healthcheck as key,value>
            ${key}: ${value}
        </#list>
        </#if>
</#list>
<#list modules.infrastructures as service>
    ${service.name}:
    <#if service.docker_service.image?has_content>
        image: ${service.docker_service.image}
    </#if>
    <#if service.docker_service.networks?has_content>
        networks: ${service.docker_service.networks}
    </#if>
    <#if service.docker_service.network_mode?has_content>
        network_mode: ${service.docker_service.network_mode}
    </#if>
    <#if service.docker_service.hostname?has_content>
        hostname: ${service.docker_service.hostname}
    </#if>
    <#if service.docker_service.restart??>
        restart: ${service.docker_service.restart}
    </#if>
    <#if service.docker_service.build?has_content>
        build: ${service.docker_service.build}
    </#if>
    <#if service.docker_service.container_name?has_content>
        container_name: ${service.docker_service.container_name}
    </#if>
    <#if service.docker_service.volumes??>
        volumes:
        <#list service.docker_service.volumes as volume>
            - ${volume}
        </#list>
    </#if>
    <#if service.docker_service.environment??>
        environment:
        <#list service.docker_service.environment as key,value>
            ${key}: ${value}
        </#list>
    </#if>
    <#if service.docker_service.ports??>
        ports:
        <#list service.docker_service.ports as port>
            - ${port}
        </#list>
    </#if>
    <#if service.docker_service.depends_on??>
        depends_on:
        <#list service.docker_service.depends_on?keys as key>
            ${key}:
                condition: ${service.docker_service.depends_on[key].condition}
        </#list>
    </#if>
    <#if service.docker_service.healthcheck??>
        healthcheck:
        <#list service.docker_service.healthcheck as key,value>
            ${key}: ${value}
        </#list>
    </#if>
</#list>
