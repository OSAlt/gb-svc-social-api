package org.geekbeacon.social.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.net.URI;
import java.util.Map;

@Builder
@Data
@ApiModel("Model represents online social activity")
public class SocialActivity {
    @ApiModelProperty("originating URI for content")
    private final URI sourceUri;
    @ApiModelProperty("URL of media to display")
    private final String mediaUrl;
    @ApiModelProperty("text/comment/caption that goes with media if any")
    private final String text;
    @ApiModelProperty("dimensions of media")
    private final Dimensions dimensions;
    @ApiModelProperty("represents media dimensions")
    private final boolean isVideo;
    @ApiModelProperty("a hashmap of all socialLove data depending on the social media platform.  likes, comments, thumbs up, +1s etc")
    private final Map<String, Object> socialLove;

}
