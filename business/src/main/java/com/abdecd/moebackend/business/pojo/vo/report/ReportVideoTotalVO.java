package com.abdecd.moebackend.business.pojo.vo.report;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;

@Accessors(chain = true)
@Data
public class ReportVideoTotalVO {
    private Integer total;
    private ArrayList<ReportVideoVO> records;
}
