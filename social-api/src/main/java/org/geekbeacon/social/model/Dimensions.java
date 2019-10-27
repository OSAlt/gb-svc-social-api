package org.geekbeacon.social.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Map;

@Data
@ApiModel("Resource dimensions , mainly used to represent an image width x height")
public class Dimensions {
    @ApiModelProperty("height of image")
    private final int height;
    @ApiModelProperty("width of image")
    private final int width;

    public Dimensions(int height, int  width) {
        this.height = height;
        this.width  = width;
    }

    public Dimensions(Map<String, Object> initData) {
        if(initData.containsKey("height")) {
            height = NumberUtils.toInt(initData.get("height").toString(), 0);
        } else {
            height = 0;
        }

        if(initData.containsKey("width")) {
            width= NumberUtils.toInt(initData.get("width").toString(), 0);
        } else {
            width= 0;
        }

    }
}
