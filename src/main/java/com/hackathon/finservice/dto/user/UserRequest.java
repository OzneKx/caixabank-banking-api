package com.hackathon.finservice.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 100)
    @JsonProperty("name")
    String name;

    @Size(max = 120)
    @JsonProperty("email")
    String email;

    @NotBlank(message = "Password is required")
    @JsonProperty("password")
    String password;
}
