package com.online.bus.ticket.reservation.booking.validator;

import com.online.bus.ticket.reservation.booking.exception.RequiredFieldsMissingException;
import com.online.bus.ticket.reservation.booking.request.Address;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class AddressValidator {

    public void validateAddressRequest(Address address) {

        if (Objects.isNull(address)) {
            log.info("[Error]: Invalid Address in Create passenger request is null");
            throw new RequiredFieldsMissingException("Invalid Address in Create passenger request is null");
        }

        if(StringUtils.isBlank(address.getBuildingNumber())) {
            log.info("[Error]: Invalid building Number of address in Create passenger request: {}", address);
            throw new RequiredFieldsMissingException("Invalid building Number of address in Create passenger request: {}"+ address);
        }

        if(StringUtils.isBlank(address.getStreetName())) {
            log.info("[Error]: Invalid street name of address in Create passenger request: {}", address);
            throw new RequiredFieldsMissingException("Invalid street name of address in Create passenger request: {}"+ address);
        }

        if(StringUtils.isBlank(address.getAddressLine())) {
            log.info("[Error]: Invalid address line of address in Create passenger request: {}", address);
            throw new RequiredFieldsMissingException("Invalid address line of address in Create passenger request: {}"+ address);
        }

        if(StringUtils.isBlank(address.getCity())) {
            log.info("[Error]: Invalid city of address in Create passenger request: {}", address);
            throw new RequiredFieldsMissingException("Invalid city of address in Create passenger request: {}"+ address);
        }

        if(StringUtils.isBlank(address.getState())) {
            log.info("[Error]: Invalid state of address in Create passenger request: {}", address);
            throw new RequiredFieldsMissingException("Invalid state of address in Create passenger request: {}"+ address);
        }

        if(StringUtils.isBlank(address.getZipCode())) {
            log.info("[Error]: Invalid zip code of address in Create passenger request: {}", address);
            throw new RequiredFieldsMissingException("Invalid zip code of address in Create passenger request: {}"+ address);
        }
    }
}
