package ru.praktikum.burgers.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ResponseOrder {
    private String message;
    private String name;
    private boolean success;
    private List<Order> orders = new ArrayList<>();
}

