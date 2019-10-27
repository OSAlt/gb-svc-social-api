package org.geekbeacon.social.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

@ApiModel("A contact entity used to map topic to email for a given domain")
public class Contact {
    @JsonIgnore
    private String domain;
    @ApiModelProperty("valid email address")
    private String email;
    @ApiModelProperty("Topic / Subject description")
    private String description;

}
