package com.driver.model;

public enum SubscriptionType {
    BASIC(0),
    PRO(1),
    ELITE(2);

    private final int priority;

    SubscriptionType(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
