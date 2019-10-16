package org.geekbeacon.social.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Contact {
    @JsonIgnore
    private String domain;
    private String email;
    private String description;

}
