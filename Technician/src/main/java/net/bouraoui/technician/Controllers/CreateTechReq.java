package net.bouraoui.technician.Controllers;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateTechReq(
        Long userID,
        @JsonProperty("category")String Category
) {
}
