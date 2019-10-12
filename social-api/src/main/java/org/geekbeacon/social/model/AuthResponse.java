package org.geekbeacon.social.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthResponse {
    final Status status;
    final String  message;
    final String url;
}
