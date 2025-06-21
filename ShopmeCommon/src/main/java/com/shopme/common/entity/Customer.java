package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "customers")
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 45, nullable = false, unique = true)
    private String email;

    @Column(length = 64, nullable = false)
    private String password;

    @Column(name = "first_name", length = 45, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 45, nullable = false)
    private String lastName;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "prefix", column = @Column(name = "phone_number_prefix", length = 5)),
            @AttributeOverride( name = "phone", column = @Column(name = "phone_number_core", length = 15, nullable = false))
    })
    private PhoneNumber phoneNumber;

    @Column(name = "address_line_1", length = 64, nullable = false)
    private String addressLine1;

    @Column(name = "address_line_2", length = 64)
    private String addressLine2;

    @Column(length = 45, nullable = false)
    private String city;

    @Column(length = 45)
    private String state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(name = "postal_code", length = 10, nullable = false)
    private String postalCode;

    @CreationTimestamp
    @Column(name = "created_time")
    private Date createdTime;

    @Column(nullable = false)
    private boolean enabled;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", addressLine1='" + addressLine1 + '\'' +
                ", addressLine2='" + addressLine2 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country=" + country +
                ", postalCode='" + postalCode + '\'' +
                ", createdTime=" + createdTime +
                ", enabled=" + enabled +
                ", verificationCode='" + verificationCode + '\'' +
                '}';
    }

    public String getFullName() {
        // joins non-null arguments with a single space, trimming extras
        return StringUtils.joinWith(" ", firstName, lastName).trim();
    }
}
