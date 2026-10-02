package jsp.springboot.courier.management.dto;

public enum ShipmentStatusEnum {
    PENDING,
    ORDER_PLACED,
    PACKED,
    SHIPPED,
    OUT_FOR_DELIVERY,
    DELIVERED,
    FAILED_DELIVERY,
    CANCELLED,
    RETURNED,
    BOOKED,
    IN_TRANSIT
}