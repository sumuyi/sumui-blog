package com.sumui.service.convert;

import com.sumui.common.model.dto.BooksDTO;
import com.sumui.common.model.finance.Books;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BooksConvert {
    Books toBooks(BooksDTO booksDTO);
}
