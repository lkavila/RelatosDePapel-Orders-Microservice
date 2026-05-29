package com.relatosdepapel.orders.facade;

import com.relatosdepapel.orders.exception.BadSupplyModificationException;
import com.relatosdepapel.orders.exception.InternalErrorException;
import com.relatosdepapel.orders.exception.SupplyNotFoundException;
import com.relatosdepapel.orders.facade.model.SupplyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CatalogFacade {

    private final WebClient.Builder webClientBuilder;
    @Value("${catalog.url}")
    private String catalogUrl;

    public SupplyDto getSupply(Integer supplyId) {
        try {
            return webClientBuilder.build()
                    .get()
                    .uri(catalogUrl + "/supplies/{id}", supplyId)
                    .retrieve()
                    .bodyToMono(SupplyDto.class)
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            throw new SupplyNotFoundException("Supply with ID " + supplyId + " not found", e);
        } catch (WebClientResponseException.InternalServerError e) {
            throw new InternalErrorException("An exception occurred fetching supply with ID " + supplyId, e);
        }
    }

    public void updateSupplyStock(Integer supplyId, Integer stock) {
        try {
            webClientBuilder.build().patch()
                    .uri(catalogUrl + "/supplies/{id}", supplyId)
                    .bodyValue(Map.of("stock", stock))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            throw new SupplyNotFoundException("Supply with ID " + supplyId + " not found", e);
        } catch (WebClientResponseException.BadRequest e) {
            throw new BadSupplyModificationException("Bad request when updating stock for supply with ID " + supplyId, e);
        } catch (WebClientResponseException.InternalServerError e) {
            throw new InternalErrorException("An exception occurred fetching supply with ID " + supplyId, e);
        }
    }

    public Map<Integer, SupplyDto> getSuppliesInBatch(List<Integer> suppliesIds) {
        try {
            return webClientBuilder.build()
                    .post()
                    .uri(catalogUrl + "/supplies/get-in-batch")
                    .bodyValue(Map.of("suppliesIds", suppliesIds))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<Integer, SupplyDto>>() {})
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            throw new SupplyNotFoundException("Supply not found", e);
        } catch (WebClientResponseException.BadRequest e) {
            throw new BadSupplyModificationException("Bad request when getting supplies in batch ", e);
        } catch (WebClientResponseException.InternalServerError e) {
            throw new InternalErrorException("An exception occurred fetching supplies in batch ", e);
        }
    }

}
