package com.element_type.parameter.model;

import com.sun.istack.internal.NotNull;
import lombok.Data;

@Data
public class User {
    @NotNull
    private String username;
    @NotNull
    private String age;
}
