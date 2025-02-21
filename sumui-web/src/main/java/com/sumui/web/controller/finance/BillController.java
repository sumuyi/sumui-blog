package com.sumui.web.controller.finance;

import com.sumui.common.constants.StatusEnum;
import com.sumui.common.model.ReqResult;
import com.sumui.common.model.dto.BillDTO;
import com.sumui.service.BillsService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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

    @GetMapping("/list/{bookId}")
    public ReqResult<List<BillDTO>> getBillList(@PathVariable Long bookId) {
        try {
            List<BillDTO> billList = billsService.getBillList(bookId);
            return ReqResult.ok(billList);
        } catch (Exception e) {
            return ReqResult.fail(StatusEnum.BILL_SAVE_ERROR);
        }
    }
}