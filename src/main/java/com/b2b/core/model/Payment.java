package com.b2b.core.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Payment {
    private String providerToken;
    private List<Invoice> invoice;
}
