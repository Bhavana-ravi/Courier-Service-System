package jsp.springboot.courier.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jsp.springboot.courier.management.dto.ResponseStructure;
import jsp.springboot.courier.management.dto.TrackingHistoryStatusEnum;
import jsp.springboot.courier.management.entity.TrackingHistory;
import jsp.springboot.courier.management.service.TrackingHistoryService;

@RestController
@RequestMapping("/api/tracking-history")
public class TrackingHistoryController {

    @Autowired
    private TrackingHistoryService trackingHistoryService;

    // 1. Get all tracking history
    @GetMapping
    public ResponseEntity<ResponseStructure<List<TrackingHistory>>> getAllTrackingHistories() {
        return new ResponseEntity<>(trackingHistoryService.getAllTrackingHistories(), HttpStatus.OK);
    }

    // 2. Get tracking history by Id
    @GetMapping("/{trackingId}")
    public ResponseEntity<ResponseStructure<TrackingHistory>> getTrackingHistoryById(@PathVariable Integer trackingId) {
        return new ResponseEntity<>(trackingHistoryService.getTrackingHistoryById(trackingId), HttpStatus.OK);
    }

    // 3. Get tracking history by shipment tracking number
    @GetMapping("/tracking-number/{shipmentTrackingNumber}")
    public ResponseEntity<ResponseStructure<List<TrackingHistory>>> getHistoryByTrackingNumber(@PathVariable Integer shipmentTrackingNumber) {
        return new ResponseEntity<>(trackingHistoryService.getHistoryByTrackingNumber(shipmentTrackingNumber), HttpStatus.OK);
    }

    // 4. Get tracking history by status
    @GetMapping("/status/{trackingStatus}")
    public ResponseEntity<ResponseStructure<List<TrackingHistory>>> getByHistoryStatus(@PathVariable TrackingHistoryStatusEnum trackingStatus) {
        return new ResponseEntity<>(trackingHistoryService.getByHistoryStatus(trackingStatus), HttpStatus.OK);
    }
}