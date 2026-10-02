package jsp.springboot.courier.management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jsp.springboot.courier.management.dto.ResponseStructure;
import jsp.springboot.courier.management.dto.TrackingHistoryStatusEnum;
import jsp.springboot.courier.management.entity.TrackingHistory;
import jsp.springboot.courier.management.exception.IdNotFoundException;
import jsp.springboot.courier.management.exception.NoRecordAvailableException;
import jsp.springboot.courier.management.repository.TrackingHistoryRepository;

@Service
public class TrackingHistoryService {

    @Autowired
    private TrackingHistoryRepository trackingHistoryRepository;

    // 1. Get all tracking history
    public ResponseStructure<List<TrackingHistory>> getAllTrackingHistories() {
        List<TrackingHistory> histories = trackingHistoryRepository.findAll();
        if (histories.isEmpty()) {
            throw new NoRecordAvailableException("No tracking history available");
        }
        ResponseStructure<List<TrackingHistory>> res = new ResponseStructure<>();
        res.setData(histories);
        res.setMessage("Tracking history fetched successfully");
        res.setStatusCode(HttpStatus.OK.value());
        return res;
    }

    // 2. Get tracking history by Id
    public ResponseStructure<TrackingHistory> getTrackingHistoryById(Integer trackingId) {
        Optional<TrackingHistory> optionalHistory = trackingHistoryRepository.findById(trackingId);
        if (optionalHistory.isEmpty()) {
            throw new IdNotFoundException("Tracking history not found with id: " + trackingId);
        }
        ResponseStructure<TrackingHistory> res = new ResponseStructure<>();
        res.setData(optionalHistory.get());
        res.setMessage("Tracking history fetched successfully");
        res.setStatusCode(HttpStatus.OK.value());
        return res;
    }

    // 3. Get tracking history by shipment tracking number
    public ResponseStructure<List<TrackingHistory>> getHistoryByTrackingNumber(Integer shipmentTrackingNumber) {
        List<TrackingHistory> histories = trackingHistoryRepository.findByShipment_ShipmentTrackingNumber(shipmentTrackingNumber);
        if (histories.isEmpty()) {
            throw new NoRecordAvailableException("No tracking history found for tracking number: " + shipmentTrackingNumber);
        }
        ResponseStructure<List<TrackingHistory>> res = new ResponseStructure<>();
        res.setData(histories);
        res.setMessage("Tracking history fetched successfully");
        res.setStatusCode(HttpStatus.OK.value());
        return res;
    }

    // 4. Get tracking history by status
    public ResponseStructure<List<TrackingHistory>> getByHistoryStatus(TrackingHistoryStatusEnum trackingStatus) {
        List<TrackingHistory> histories = trackingHistoryRepository.findByTrackingStatus(trackingStatus);
        if (histories.isEmpty()) {
            throw new NoRecordAvailableException("No tracking history found with status: " + trackingStatus);
        }
        ResponseStructure<List<TrackingHistory>> res = new ResponseStructure<>();
        res.setData(histories);
        res.setMessage("Tracking history fetched successfully");
        res.setStatusCode(HttpStatus.OK.value());
        return res;
    }
}