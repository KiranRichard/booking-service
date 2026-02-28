package com.online.bus.ticket.reservation.booking.validator;

import com.online.bus.ticket.reservation.booking.enums.Gender;
import com.online.bus.ticket.reservation.booking.exception.RequiredFieldsMissingException;
import com.online.bus.ticket.reservation.booking.request.PassengerCancelRequest;
import com.online.bus.ticket.reservation.booking.request.TicketCancelRequest;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class TicketCancelRequestValidator {

    public void validateTicketCancelRequest(TicketCancelRequest ticketCancelRequest) {

        if (Objects.isNull(ticketCancelRequest)) {
            log.info("[Error] : The ticket cancel request is null");
            throw new RequiredFieldsMissingException("The ticket cancel request is null");
        }

        if (StringUtils.isBlank(ticketCancelRequest.getReasonForCancellation())) {
            log.info("[Error] : The Reason for cancellation is null in ticket cancel request");
            throw new RequiredFieldsMissingException("The ReasonForCancellation is null in ticket cancel request");
        }

        if (Objects.isNull(ticketCancelRequest.getCancellationDateTime())) {
            log.info("[Error] : The Cancellation Date is null in ticket cancel request");
            throw new RequiredFieldsMissingException("The CancellationDateTime is null in ticket cancel request");
        }

        if (CollectionUtils.isEmpty(ticketCancelRequest.getPassengerCancelRequests())) {
            log.info("[Error] : The PassengerCancelRequests is null in ticket cancel request");
            throw new RequiredFieldsMissingException("The PassengerCancelRequests is null in ticket cancel request");
        }

        validatePassengerCancelRequest(ticketCancelRequest.getPassengerCancelRequests());
    }

    private void validatePassengerCancelRequest(List<PassengerCancelRequest> passengerCancelRequests) {

        for (PassengerCancelRequest passengerCancelRequest : passengerCancelRequests) {
            if (passengerCancelRequest.getPassengerId()<=0) {
                log.info("[Error] : The Passenger Id is null in ticket cancel request");
                throw new RequiredFieldsMissingException("The Passenger Id is null in ticket cancel request");
            }

            if (StringUtils.isBlank(passengerCancelRequest.getFirstName())) {
                log.info("[Error] : The First Name is null in ticket cancel request");
                throw new RequiredFieldsMissingException("The First Name is null in ticket cancel request");
            }

            if (StringUtils.isBlank(passengerCancelRequest.getLastName())) {
                log.info("[Error] : The Last Name is null in ticket cancel request");
                throw new RequiredFieldsMissingException("The Last Name is null in ticket cancel request");
            }

            if (passengerCancelRequest.getAge()<=0) {
                log.info("[Error] : The passenger age is null in ticket cancel request");
                throw new RequiredFieldsMissingException("The Method… is null in ticket cancel request");
            }

            if (StringUtils.isBlank(passengerCancelRequest.getGender()) || Objects.isNull(Gender.findByName(passengerCancelRequest.getGender()))) {
                log.info("[Error] : Invalid Gender in ticket cancel request");
                throw new RequiredFieldsMissingException("Invalid Gender in ticket cancel request");
            }

            if (StringUtils.isBlank(passengerCancelRequest.getEmailId())) {
                log.info("[Error] : The Email Id is null in ticket cancel request");
                throw new RequiredFieldsMissingException("The Email Id is null in ticket cancel request");
            }

            if (StringUtils.isBlank(passengerCancelRequest.getContactNumber())) {
                log.info("[Error] : The Contact Number is null in ticket cancel request");
                throw new RequiredFieldsMissingException("The Contact Number  is null in ticket cancel request");
            }
        }
    }
}