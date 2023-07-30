package com.boardify.boardify.DTO;


import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MechanicsResponse {

    private List<MechanicsSearchResult> mechanics;

}
