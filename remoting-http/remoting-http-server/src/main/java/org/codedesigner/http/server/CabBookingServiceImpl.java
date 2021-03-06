package org.codedesigner.http.server;

import org.codedesigner.remoting.api.Booking;
import org.codedesigner.remoting.api.BookingException;
import org.codedesigner.remoting.api.CabBookingService;
import org.springframework.stereotype.Component;

import static java.lang.Math.random;
import static java.util.UUID.randomUUID;

@Component
public class CabBookingServiceImpl implements CabBookingService {

    @Override
    public Booking bookRide(String pickUpLocation) throws BookingException {
        if (random() < 0.3) throw new BookingException("Cab unavailable");
        return new Booking(randomUUID().toString());
    }
}
