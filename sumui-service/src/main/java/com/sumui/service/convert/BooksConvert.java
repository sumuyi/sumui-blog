package com.sumui.service.convert;

import com.sumui.common.model.dto.BillDTO;
import com.sumui.common.model.dto.BooksDTO;
import com.sumui.common.model.finance.Bills;
import com.sumui.common.model.finance.Books;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BooksConvert {
    Books toBooks(BooksDTO booksDTO);

    // 批量转换方法
    List<BooksDTO> toDTOList(List<Books> booksList);
}
