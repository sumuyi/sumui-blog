package com.sumui.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sumui.common.constants.StatusEnum;
import com.sumui.service.convert.BillConverter;
import com.sumui.common.exception.BizException;
import com.sumui.common.model.dto.BillDTO;
import com.sumui.common.model.finance.Bills;
import com.sumui.dao.mapper.finance.BillsMapper;
import com.sumui.service.BillsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
* @author flk-sunl
* @description 针对表【bills(账单表)】的数据库操作Service实现
* @createDate 2025-02-21 10:21:17
*/
@Slf4j
@Service
public class BillsServiceImpl extends ServiceImpl<BillsMapper, Bills> implements BillsService{

    @Resource
    private BillConverter billConverter;

    /**
     * 添加账单
     * @param billDTO 账单信息
     */
    @Override
    public void saveBill(BillDTO billDTO) {
        log.error("billDto:{}", billDTO);
        // 参数校验
        if (billDTO.getAmount() == null || billDTO.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new BizException(StatusEnum.BILL_AMOUNT_INVALID);
        }

        if (billDTO.getCategoryId() == null) {
            throw new BizException(StatusEnum.BILL_CATEGORY_EMPTY);
        }

        try {
            Bills bill = billConverter.toBill(billDTO);
            bill.setId(IdUtil.getSnowflakeNextId());
            // todo 默认账本ID为1
            bill.setBookId(1L);
            bill.setPaymentMethod("wechat");
            bill.setCreatedBy(1L);
            log.error("bill:{}", bill);
            this.save(bill);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BizException(StatusEnum.BILL_SAVE_ERROR);
        }
    }

    /**
     * @param bookId
     * @return
     */
    @Override
    public List<BillDTO> getBillList(Long bookId) {
        if (bookId == null) {
            return ListUtil.empty();
        }
        List<Bills> billsList = this.lambdaQuery().eq(Bills::getBookId, bookId).list();
        if (CollectionUtil.isNotEmpty(billsList)) {
            return billConverter.toDTOList(billsList);
        }
        return ListUtil.of();
    }


}




