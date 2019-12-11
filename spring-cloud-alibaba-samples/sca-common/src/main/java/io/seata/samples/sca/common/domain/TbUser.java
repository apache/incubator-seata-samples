package io.seata.samples.sca.common.domain;

import lombok.Data;

import java.io.Serializable;
@Data
public class TbUser implements Serializable{
    /**
     *  
     */
    private Integer id;

    /**
     *  
     */
    private String name;

    /**
     *  
     */
    private Integer age;
}