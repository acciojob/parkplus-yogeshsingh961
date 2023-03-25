package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        ParkingLot parkingLot;
        try{
            parkingLot=parkingLotRepository3.findById(parkingLotId).get();
        }catch (Exception e){
            throw new Exception("parkingLot not found");
        }

        User user;
        try{
            user=userRepository3.findById(userId).get();
        }catch (Exception e){
            throw new Exception("user not found");
        }

        Reservation reservation= new Reservation(timeInHours);

        List<Spot>spotList=parkingLot.getSpotList();
        Spot spot=null;
        int price=Integer.MAX_VALUE;
        for(Spot spot1:spotList){
            int wheels;
            if(spot1.getSpotType()==SpotType.FOUR_WHEELER){
                wheels=4;
            }else if(spot1.getSpotType()==SpotType.TWO_WHEELER) wheels=2;
            else wheels=5;
            if(!spot1.isOccupied() && wheels>numberOfWheels && spot1.getPricePerHour()<price){
                spot=spot1;
                price=spot1.getPricePerHour();
            }
        }
        if(spot==null) throw new Exception("cannot make reservation");

        reservation.setUser(user);
        reservation.setSpot(spot);
        spot.setOccupied(true);
        user.getReservationList().add(reservation);
        spot.getReservations().add(reservation);
        spot.setParkingLot(parkingLot);
        parkingLotRepository3.save(parkingLot);
        userRepository3.save(user);
        return reservation;
    }
}
