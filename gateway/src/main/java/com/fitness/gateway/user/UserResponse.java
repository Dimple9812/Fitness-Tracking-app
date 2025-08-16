//whatever response seen by user will be mentioned here
package com.fitness.gateway.user;
import lombok.Data;

import java.time.LocalDateTime;

@Data //data annotation for generating getters and setters
public class UserResponse {
    private String id;
    private String keyCloakId;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
