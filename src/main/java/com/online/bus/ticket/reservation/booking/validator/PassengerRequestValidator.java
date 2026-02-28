package com.online.bus.ticket.reservation.booking.validator;

import com.online.bus.ticket.reservation.booking.enums.Gender;
import com.online.bus.ticket.reservation.booking.exception.RequiredFieldsMissingException;
import com.online.bus.ticket.reservation.booking.request.PassengerRequest;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class PassengerRequestValidator {

    public void validatePassengerRequest(PassengerRequest passengerRequest) {

        if (Objects.isNull(passengerRequest)) {
            log.info("[Error]: Invalid Create passenger request is null");
            throw new RequiredFieldsMissingException("Invalid Create passenger request is null");
        }

        if(StringUtils.isBlank(passengerRequest.getFirstName())) {
            log.info("[Error]: Invalid first name in Create passenger request: {}", passengerRequest);
            throw new RequiredFieldsMissingException("Invalid first name in Create passenger request: {}"+ passengerRequest);
        }

        if(StringUtils.isBlank(passengerRequest.getLastName())) {
            log.info("[Error]: Invalid last name in Create passenger request: {}", passengerRequest);
            throw new RequiredFieldsMissingException("Invalid last name in Create passenger request: {}"+ passengerRequest);
        }

        if(passengerRequest.getAge()<=0) {
            log.info("[Error]: Invalid age in Create passenger request: {}", passengerRequest);
            throw new RequiredFieldsMissingException("Invalid age in Create passenger request: {}"+ passengerRequest);
        }

        if(StringUtils.isBlank(passengerRequest.getGender()) || Objects.isNull(Gender.findByName(passengerRequest.getGender()))) {
            log.info("[Error]: Invalid gender in Create passenger request: {}", passengerRequest);
            throw new RequiredFieldsMissingException("Invalid gender in Create passenger request: {}"+ passengerRequest);
        }

        if(StringUtils.isBlank(passengerRequest.getEmailId())) {
            log.info("[Error]: Invalid email Id in Create passenger request: {}", passengerRequest);
            throw new RequiredFieldsMissingException("Invalid Email id in Create passenger request: {}"+ passengerRequest);
        }

        if(StringUtils.isBlank(passengerRequest.getContactNumber())) {
            log.info("[Error]: Invalid contact number in Create passenger request: {}", passengerRequest);
            throw new RequiredFieldsMissingException("Invalid contact number in Create passenger request: {}"+ passengerRequest);
        }
    }

    public void validatePassengerId(long passengerId) {
        if (passengerId<=0) {
            log.info("[Error]: Invalid passengerId field in request: {}", passengerId);
            throw new RequiredFieldsMissingException("Invalid passengerId field in request: {}"+ passengerId);
        }
    }
}
