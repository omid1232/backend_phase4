package com.example.demo.dto;


import lombok.Data;

@Data
public class FollowedDesignerDTO {
    private String designerId;

    public String getDesignerId() {
        return this.designerId;
    }
}

