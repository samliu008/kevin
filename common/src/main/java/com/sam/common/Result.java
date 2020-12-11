package com.sam.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: DELL
 * @Date: 2020/12/11 17:04
 * @Description:
 */
@Data
public class Result<T> implements Serializable {
    /** 状态码 */
    private int code;
    /** 消息 */
    private String msg;
    /** 数据 */
    private T data;
}
