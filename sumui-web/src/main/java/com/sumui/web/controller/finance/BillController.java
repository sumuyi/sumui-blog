package com.sumui.web.controller.finance;

import com.sumui.common.constants.StatusEnum;
import com.sumui.common.model.ReqResult;
import com.sumui.common.model.dto.BillDTO;
import com.sumui.service.BillsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Api("账单管理")
@RestController
@RequestMapping("/bill")
public class BillController {

    @Resource
    private BillsService billsService;

    @PostMapping("/add")
    public ReqResult<Void> saveBill(@RequestBody BillDTO billDTO) {
        try {
            billsService.saveBill(billDTO);
            return ReqResult.ok();
        } catch (Exception e) {
            return ReqResult.fail(StatusEnum.BILL_SAVE_ERROR);
        }
    }

    @ApiOperation("获取账单列表-按天分组")
    @GetMapping("/list/{bookId}/{month}")
    public ReqResult<Map<String, List<BillDTO>>> getBillList(@PathVariable Long bookId, @PathVariable String month) {
        try {
            Map<String, List<BillDTO>> billList = billsService.getBillList(bookId, month);
            return ReqResult.ok(billList);
        } catch (Exception e) {
            return ReqResult.fail(StatusEnum.BILL_SAVE_ERROR);
        }
    }

    @ApiOperation("获取当月账单统计")
    @GetMapping("statistics/{bookId}/{month}")
    public ReqResult<Map<String, Object>> getBillStatistics(@PathVariable Long bookId, @PathVariable String month) {
        try {
            Map<String, Object> billStatistics = billsService.getBillStatistics(bookId, month);
            return ReqResult.ok(billStatistics);
        } catch (Exception e) {
            return ReqResult.fail(StatusEnum.BILL_SAVE_ERROR);
        }
    }

    @ApiOperation("删除账单")
    @DeleteMapping("delete/{id}")
    public ReqResult<Map<String, Object>> getBillStatistics(@PathVariable String id) {
        try {
            billsService.removeById(id);
            return ReqResult.ok();
        } catch (Exception e) {
            return ReqResult.fail(StatusEnum.BILL_SAVE_ERROR);
        }
    }
}
