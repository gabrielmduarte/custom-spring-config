package com.gabrielduarte.customconfigpoc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@AllArgsConstructor
public class UserEntity {

    @Id
    private String id;

}
