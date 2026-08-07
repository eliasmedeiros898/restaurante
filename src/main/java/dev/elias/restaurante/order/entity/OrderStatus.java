package dev.elias.restaurante.order.entity;

public enum OrderStatus {
    AWAITING_CONFIRMATION,
    CONFIRMED,
    PREPARING,
    READY,
    DELIVERED,
    CANCELLED
}