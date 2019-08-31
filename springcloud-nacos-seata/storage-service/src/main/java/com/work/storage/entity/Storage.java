package com.work.storage.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Program Name: springcloud-nacos-seata
 * <p>
 * Description:
 * <p>
 *
 * @author zhangjianwei
 * @version 1.0
 * @date 2019/8/28 4:05 PM
 */
@Data
@Accessors(chain = true)
@TableName("storage_tbl")
public class Storage {

  private Long id;
  private String commodityCode;
  private Long count;


}
