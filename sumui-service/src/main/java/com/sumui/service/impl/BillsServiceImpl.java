package com.sumui.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
     * @param month
     * @return
     */
    @Override
    public Map<String, List<BillDTO>> getBillList(Long bookId, String month) {
        if (bookId == null || StrUtil.isEmpty(month)) {
            throw new BizException(StatusEnum.ILLEGAL_ARGUMENTS);
        }
        // 转换当月第一天和最后一天
        String startDate = month + "-01";
        String endDate = month + "-" + LocalDate.parse(startDate).lengthOfMonth();
        List<Bills> billsList = this.lambdaQuery()
                .eq(Bills::getBookId, bookId)
                .ge(Bills::getBillDate, startDate)
                .le(Bills::getBillDate, endDate)
                .list();
        if (CollectionUtil.isNotEmpty(billsList)) {
            List<BillDTO> dtoList = billConverter.toDTOList(billsList);
            // todo 这里用户先写死
            dtoList.forEach(vo -> {
                if (vo.getUserId() != null && vo.getUserId() == 1L) {
                    vo.setUserName("孙罗");
                }
                if (vo.getUserId()!= null && vo.getUserId() == 2L) {
                    vo.setUserName("关甜");
                }
            });
            // 按日期分组 并降序
            dtoList.sort((o1, o2) -> o2.getBillDate().compareTo(o1.getBillDate()));
            // key指定日期格式
            return dtoList.stream().collect(Collectors.groupingBy(vo -> DateUtil.format(vo.getBillDate(), "yyyy-MM-dd")));
        }
        return MapUtil.empty();
    }

    @Override
    public Map<String, Object> getBillStatistics(Long bookId, String month) {
        // 参数判断
        if (bookId == null || StrUtil.isBlank(month)) {
            throw new BizException(StatusEnum.ILLEGAL_ARGUMENTS);
        }
        // 转换当月第一天和最后一天
        String startDate = month + "-01";
        String endDate = month + "-" + LocalDate.parse(startDate).lengthOfMonth();
        // todo 暂时直接通过sql判断当月记录，后期单独一张统计表进行维护
        List<Bills> list = this.lambdaQuery().eq(Bills::getBookId, bookId)
                .ge(Bills::getBillDate, startDate)
                .le(Bills::getBillDate, endDate)
                .list();
        if (CollectionUtil.isEmpty(list)) {
            return MapUtil.newHashMap();
        }
        // 统计支出和收入金额
        Map<String, Object> resultMap = MapUtil.newHashMap();
        BigDecimal expenseAmount = list.stream().filter(vo -> vo.getType() == 1)
                .map(Bills::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal incomeAmount = list.stream().filter(vo -> vo.getType() == 0)
               .map(Bills::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        resultMap.put("totalExpense", expenseAmount);
        resultMap.put("totalIncome", incomeAmount);
        return resultMap;
    }


}




