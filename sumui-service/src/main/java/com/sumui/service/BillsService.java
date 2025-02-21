package com.sumui.service;

import com.sumui.common.constants.StatusEnum;
import com.sumui.common.model.dto.BillDTO;
import com.sumui.common.model.finance.Bills;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author flk-sunl
* @description 针对表【bills(账单表)】的数据库操作Service
* @createDate 2025-02-21 10:21:17
*/
public interface BillsService extends IService<Bills> {
    void saveBill(BillDTO billDTO);

    List<BillDTO> getBillList(Long bookId);
}
