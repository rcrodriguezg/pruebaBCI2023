package com.rrg.springbootjpah2.dto;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class UserDTO {


        UUID id;

        @NotNull
        @NotBlank(message = "First name is mandatory")
        @Pattern(regexp = "^[A-Za-z0-9]+$", message = "An alphanumeric first name is mandatory")
        private String name;

        @NotNull
        private String email;

        public String getEmail() {
                return this.email;
        }

        @NotNull
        private String password;

        private PhonesDTO phones;

        private Date created;

        private Date modified;

        private Date lastLogin;


        private Boolean isActive;

}
