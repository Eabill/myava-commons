package com.myava.springboot.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class RedPacketInfo implements Serializable {

    private Long id;

    private String redPacketNo;

    private String redPacketName;

    private BigDecimal totalAmount;

    private int totalNumber;

    private Date startDate;

    private Date endDate;

    private static final long serialVersionUID = -5280106614799291367L;
}
