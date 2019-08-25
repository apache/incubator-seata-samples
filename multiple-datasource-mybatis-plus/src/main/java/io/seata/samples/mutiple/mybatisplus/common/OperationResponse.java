package io.seata.samples.mutiple.mybatisplus.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 操作返回结果
 *
 * @author HelloWoodes
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperationResponse {

    private boolean success;

    private String message;

    private Object data;
}
