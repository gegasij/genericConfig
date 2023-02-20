package com.b2b.core.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Invoice {
    private String invoiceCommand;
    private String title;
    private String description;
    private String payload;
    private String currency;
    private List<Price> price;
}
