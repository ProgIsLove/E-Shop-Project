package com.shopme.common.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class PhoneNumber {

    private String prefix;

    private String phone;
}
