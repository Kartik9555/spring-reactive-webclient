package com.example.practice.springreactivewebclient.client;

import com.example.practice.springreactivewebclient.client.dto.BeerDTO;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Map;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BeerClient {

    Flux<JsonNode> listBeersJsonNode();
    Flux<Map> listBeersMap();
    Flux<String> listBeersString();
    Flux<BeerDTO> listBeers();

    Flux<BeerDTO> listBeersByBeerStyle(String beerStyle);

    Mono<BeerDTO> getBeerById(Integer beerId);

    Mono<BeerDTO> saveBeer(BeerDTO beerDTO);

    Mono<BeerDTO> updateBeer(BeerDTO beerDTO);
    Mono<BeerDTO> patchBeer(BeerDTO beerDTO);

    Mono<Void> deleteBeerById(Integer beerId);
}
