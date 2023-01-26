package com.statista.code.challenge.domainobjects;

public enum Currency {
    USD("$"), EURO("€"), YUAN("¥");

    private final String symbol;

    Currency(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}