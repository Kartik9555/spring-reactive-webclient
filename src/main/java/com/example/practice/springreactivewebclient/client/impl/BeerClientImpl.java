package com.example.practice.springreactivewebclient.client.impl;

import com.example.practice.springreactivewebclient.client.BeerClient;
import com.example.practice.springreactivewebclient.client.dto.BeerDTO;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BeerClientImpl implements BeerClient {
    public static final String BEER_PATH = "/api/v2/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";
    private final WebClient webClient;


    public BeerClientImpl(WebClient.Builder weBuilder) {
        this.webClient = weBuilder.build();
    }


    @Override
    public Flux<JsonNode> listBeersJsonNode() {
        return webClient.get()
            .uri(BEER_PATH)
            .retrieve()
            .bodyToFlux(JsonNode.class);
    }


    @Override
    public Flux<Map> listBeersMap() {
        return webClient.get()
            .uri(BEER_PATH)
            .retrieve()
            .bodyToFlux(Map.class);
    }


    @Override
    public Flux<String> listBeersString() {
        return webClient.get()
            .uri(BEER_PATH)
            .retrieve()
            .bodyToFlux(String.class);
    }


    @Override
    public Flux<BeerDTO> listBeers() {
        return webClient.get()
            .uri(BEER_PATH)
            .retrieve()
            .bodyToFlux(BeerDTO.class);
    }


    @Override
    public Flux<BeerDTO> listBeersByBeerStyle(String beerStyle) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder.path(BEER_PATH)
                .queryParam("beerStyle", beerStyle)
                .build()
            ).retrieve()
            .bodyToFlux(BeerDTO.class);
    }


    @Override
    public Mono<BeerDTO> getBeerById(Integer beerId) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder.path(BEER_PATH_ID).build(beerId))
            .retrieve()
            .bodyToMono(BeerDTO.class);
    }


    @Override
    public Mono<BeerDTO> saveBeer(BeerDTO beerDTO) {
        return webClient.post()
            .uri(BEER_PATH)
            .body(Mono.just(beerDTO), BeerDTO.class)
            .retrieve()
            .toBodilessEntity()
            .flatMap(voidResponseEntity -> Mono.just(voidResponseEntity.getHeaders().get(HttpHeaders.LOCATION).get(0)))
            .map(path -> path.split("/")[path.split("/").length - 1])
            .flatMap(beerId -> getBeerById(Integer.valueOf(beerId)));
    }


    @Override
    public Mono<BeerDTO> updateBeer(BeerDTO beerDTO) {
        return webClient.put()
            .uri(uriBuilder -> uriBuilder.path(BEER_PATH_ID).build(beerDTO.getId()))
            .body(Mono.just(beerDTO), BeerDTO.class)
            .retrieve()
            .toBodilessEntity()
            .flatMap(voidResponseEntity -> getBeerById(beerDTO.getId()));
    }


    @Override
    public Mono<BeerDTO> patchBeer(BeerDTO beerDTO) {
        return webClient.patch()
            .uri(uriBuilder -> uriBuilder.path(BEER_PATH_ID).build(beerDTO.getId()))
            .body(Mono.just(beerDTO), BeerDTO.class)
            .retrieve()
            .toBodilessEntity()
            .flatMap(voidResponseEntity -> getBeerById(beerDTO.getId()));
    }


    @Override
    public Mono<Void> deleteBeerById(Integer beerId) {
        return webClient.delete()
            .uri(uriBuilder -> uriBuilder.path(BEER_PATH_ID).build(beerId))
            .retrieve()
            .toBodilessEntity()
            .then();
    }
}
