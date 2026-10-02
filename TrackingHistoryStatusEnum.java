package jsp.springboot.courier.management.dto;

public enum TrackingHistoryStatusEnum {
    ORDER_PLACED,
    PENDING,
    BOOKED,
    PICKED_UP,
    PACKED,
    SHIPPED,
    ARRIVED_AT_HUB,
    DEPARTED_HUB,
    IN_TRANSIT,
    OUT_FOR_DELIVERY,
    DELIVERED,
    DELIVERY_FAILED, 
    RETURNED,
    CANCELLED
}