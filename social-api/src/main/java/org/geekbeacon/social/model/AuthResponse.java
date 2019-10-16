package org.geekbeacon.social.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@ApiModel("The response to an authorization.  If successful, a Status.SUCCESS and a url is returned, otherwise a failure status and a message")
public class AuthResponse {
    @ApiModelProperty("Response status")
    final Status status;
    @ApiModelProperty("Error message if any")
    final String  message;
    @ApiModelProperty("Redirect URL")
    final String url;
}
