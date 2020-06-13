package com.myava.springboot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 红包领取对象
 *
 * @author biao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RedPacketReceive implements Serializable {

    private Long redPacketId;

    private int amount;

    private Date expireDate;

    private Long userId;

    private static final long serialVersionUID = -5280106614799291367L;
}
