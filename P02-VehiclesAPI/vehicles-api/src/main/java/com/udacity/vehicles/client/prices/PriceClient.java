package com.udacity.vehicles.client.prices;

import com.udacity.vehicles.domain.car.Car;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Implements a class to interface with the Pricing Client for price data.
 */
@Component
public class PriceClient {

    private static final Logger log = LoggerFactory.getLogger(PriceClient.class);

    private final WebClient client;

    public PriceClient(WebClient pricing) {
        this.client = pricing;
    }

    // In a real-world application we'll want to add some resilience
    // to this method with retries/CB/failover capabilities
    // We may also want to cache the results so we don't need to
    // do a request every time
    /**
     * Gets a vehicle price from the pricing client, given vehicle ID.
     * @param vehicleId ID number of the vehicle for which to get the price
     * @return Currency and price of the requested vehicle,
     *   error message that the vehicle ID is invalid, or note that the
     *   service is down.
     */
    public String getPrice(Long vehicleId) {
        try {
            Price price = client
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/prices/" + vehicleId)
                            .build()
                    )
                    .retrieve().bodyToMono(Price.class).block();

            return String.format("%s %s", price.getCurrency(), price.getTotalPrice());

        } catch (Exception e) {
            log.error("Unexpected error retrieving price for vehicle {}", vehicleId, e);
        }
        return "(consult price)";
    }

    public Price savePrice(Car car) {
        Price price = new Price();
        price.setVehicleId(car.getId());
        price.setCurrency("USD");
        price.setTotalPrice(PriceClient.randomPrice());
        try {

            return client
                    .post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/prices")
                            .build()
                    )
                    .body(BodyInserters.fromObject(price))
                    .retrieve().bodyToMono(Price.class).block();

        } catch (Exception e) {
            log.error("Unexpected error saving price for vehicle {}", car.getId(), e);
        }
        return null;
    }

    private static BigDecimal randomPrice() {
        return new BigDecimal(ThreadLocalRandom.current().nextDouble(1, 5))
                .multiply(new BigDecimal(5000d)).setScale(2, RoundingMode.HALF_UP);
    }
}
