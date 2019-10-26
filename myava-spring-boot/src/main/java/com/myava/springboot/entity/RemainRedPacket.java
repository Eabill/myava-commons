package com.myava.springboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemainRedPacket implements Serializable {

    private int remainNum;

    private int remainAmt;

    private static final long serialVersionUID = -5280106614799291367L;
}
