package com.boardify.boardify.DTO;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoryResponse {

    private List<CategorySearchResult> categories;


}
