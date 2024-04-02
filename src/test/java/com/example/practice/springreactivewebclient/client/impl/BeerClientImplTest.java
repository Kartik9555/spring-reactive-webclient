package com.example.practice.springreactivewebclient.client.impl;

import com.example.practice.springreactivewebclient.client.BeerClient;
import com.example.practice.springreactivewebclient.client.dto.BeerDTO;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.awaitility.Awaitility.await;

@SpringBootTest
class BeerClientImplTest {

    @Autowired
    BeerClient client;

    @Test
    void listBeersString() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        client.listBeers().subscribe(response -> {
            System.out.println(response);
            atomicBoolean.set(true);
        });
        await().untilTrue(atomicBoolean);
    }

    @Test
    void listBeersMap() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        client.listBeersMap().subscribe(response -> {
            System.out.println(response);
            atomicBoolean.set(true);
        });
        await().untilTrue(atomicBoolean);
    }

    @Test
    void listBeersJsonNode() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        client.listBeersJsonNode().subscribe(response -> {
            System.out.println(response.toPrettyString());
            atomicBoolean.set(true);
        });
        await().untilTrue(atomicBoolean);
    }

    @Test
    void listBeers(){
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        client.listBeers().subscribe(response -> {
            System.out.println(response.getBeerName());
            atomicBoolean.set(true);
        });
        await().untilTrue(atomicBoolean);
    }

    @Test
    void listBeersByBeerStyle(){
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        client.listBeersByBeerStyle("IPA")
            .subscribe(beerDTO -> {
                System.out.println(beerDTO.getBeerName());
                atomicBoolean.set(true);
            });
        await().untilTrue(atomicBoolean);
    }

    @Test
    void getBeerById(){
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        client.listBeers()
            .flatMap(beerDTO -> client.getBeerById(beerDTO.getId()))
            .subscribe(beerDTO -> {
                System.out.println(beerDTO.getBeerName());
                atomicBoolean.set(true);
            });
        await().untilTrue(atomicBoolean);
    }

    @Test
    void saveBeer(){
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        BeerDTO beerDTO = BeerDTO.builder()
            .beerName("Test Beer")
            .beerStyle("IPA")
            .upc("123456")
            .price(BigDecimal.TEN)
            .quantityOnHand(100)
            .build();

        client.saveBeer(beerDTO)
            .subscribe(savedBeer -> {
                System.out.println(savedBeer);
                atomicBoolean.set(true);
            });

        await().untilTrue(atomicBoolean);
    }

    @Test
    void updateBeer(){
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        final String name = "New name";
        client.listBeers()
            .next()
            .doOnNext(beerDTO -> beerDTO.setBeerName(name))
            .flatMap(beerDTO -> client.updateBeer(beerDTO))
            .subscribe(beerDTO -> {
                System.out.println(beerDTO);
                atomicBoolean.set(true);
            });
        await().untilTrue(atomicBoolean);
    }

    @Test
    void patchBeer(){
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        final String name = "New name";
        client.listBeers()
            .next()
            .doOnNext(beerDTO -> beerDTO.setBeerName(name))
            .flatMap(beerDTO -> client.patchBeer(beerDTO))
            .subscribe(beerDTO -> {
                System.out.println(beerDTO);
                atomicBoolean.set(true);
            });
        await().untilTrue(atomicBoolean);
    }

    @Test
    void deleteBeer(){
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        client.listBeers()
            .next()
            .flatMap(beerDTO -> client.deleteBeerById(beerDTO.getId()))
            .doOnSuccess(unused -> atomicBoolean.set(true))
            .subscribe();
        await().untilTrue(atomicBoolean);
    }
}