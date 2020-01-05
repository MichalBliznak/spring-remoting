package org.codedesigner.remoting.api;

public interface CabBookingService {
    Booking bookRide(String pickUpLocation) throws BookingException;
}
