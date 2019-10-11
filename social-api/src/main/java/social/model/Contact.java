package social.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Contact {
    @JsonIgnore
    private final String domain;
    private final String email;
    private final String description;

}
