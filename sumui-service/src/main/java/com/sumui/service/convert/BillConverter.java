package com.sumui.service.convert;

import com.sumui.common.model.dto.BillDTO;
import com.sumui.common.model.finance.Bills;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BillConverter {
    
    // DTO 转实体类
    Bills toBill(BillDTO billDTO);
    
    // 实体类转 DTO
    BillDTO toDTO(Bills bill);
    
    // 批量转换方法
    List<BillDTO> toDTOList(List<Bills> billList);
}