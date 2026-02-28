package com.online.bus.ticket.reservation.booking.service;

import com.online.bus.ticket.reservation.booking.enums.Gender;
import com.online.bus.ticket.reservation.booking.exception.PassengerException;
import com.online.bus.ticket.reservation.booking.model.Passenger;
import com.online.bus.ticket.reservation.booking.repository.PassengerRepository;
import com.online.bus.ticket.reservation.booking.request.PassengerRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class PassengerService {

    private final PassengerRepository passengerRepository;

    public Passenger createPassenger(PassengerRequest passengerRequest){
        Passenger passenger = buildPassenger(passengerRequest, new Passenger());
        return passengerRepository.save(passenger);
    }

    public Passenger getPassenger(long passengerId) {
        Passenger passenger = passengerRepository.findById(passengerId).orElse(null);
        if (Objects.isNull(passenger)){
            throw new PassengerException("Passenger not present");
        }
        return passenger;
    }

    public Passenger editPassenger(PassengerRequest passengerRequest, long passengerId) {
        Passenger passenger = passengerRepository.findById(passengerId).orElse(null);
        if (Objects.isNull(passenger)){
            throw new PassengerException("Passenger Id is not present and unable to update");
        }
        return passengerRepository.save(buildPassenger(passengerRequest, passenger));
    }

    public void deletePassenger(long passengerId) {
        if (passengerRepository.findById(passengerId).isEmpty()) {
            throw new PassengerException("Passenger Id is not present and unable to delete");
        }
        passengerRepository.deleteById(passengerId);
    }

    public List<Passenger> getPassengers() {
        return (List<Passenger>) passengerRepository.findAll();
    }

    private Passenger buildPassenger(PassengerRequest passengerRequest, Passenger passenger) {
        passenger.setFirstName(passengerRequest.getFirstName());
        passenger.setLastName(passengerRequest.getLastName());
        passenger.setAge(passengerRequest.getAge());
        passenger.setGender(Gender.findByName(passengerRequest.getGender()).name());
        passenger.setEmailId(passengerRequest.getEmailId());
        passenger.setContactNumber(passengerRequest.getContactNumber());
        passenger.setBuildingNumber(passengerRequest.getAddress().getBuildingNumber());
        passenger.setStreetName(passengerRequest.getAddress().getStreetName());
        passenger.setAddressLine(passengerRequest.getAddress().getAddressLine());
        passenger.setCity(passengerRequest.getAddress().getCity());
        passenger.setState(passengerRequest.getAddress().getState());
        passenger.setZipCode(passengerRequest.getAddress().getZipCode());
        return passenger;
    }
}
