package dev.elias.restaurante.delivery.service;

import dev.elias.restaurante.delivery.dto.DeliveryZoneResponse;
import dev.elias.restaurante.delivery.dto.SaveDeliveryZoneRequest;
import dev.elias.restaurante.delivery.entity.DeliveryZone;
import dev.elias.restaurante.delivery.repository.DeliveryZoneRepository;
import dev.elias.restaurante.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeliveryZoneService {

    private final DeliveryZoneRepository repository;

    public DeliveryZoneService(
            DeliveryZoneRepository repository
    ) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<DeliveryZoneResponse> findPublicActiveZones() {
        return repository
                .findByActiveTrueOrderByDisplayOrderAscNeighborhoodAsc()
                .stream()
                .map(DeliveryZoneResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DeliveryZoneResponse> findAll() {
        return repository
                .findAllByOrderByDisplayOrderAscNeighborhoodAsc()
                .stream()
                .map(DeliveryZoneResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public DeliveryZone findActiveEntity(Long id) {
        DeliveryZone zone = findEntity(id);

        if (!Boolean.TRUE.equals(zone.getActive())) {
            throw new IllegalArgumentException(
                    "O bairro selecionado não está disponível para entrega"
            );
        }

        return zone;
    }

    @Transactional
    public DeliveryZoneResponse create(
            SaveDeliveryZoneRequest request
    ) {
        DeliveryZone zone = new DeliveryZone(
                request.neighborhood(),
                request.deliveryFee(),
                request.displayOrder()
        );

        return DeliveryZoneResponse.from(
                repository.save(zone)
        );
    }

    @Transactional
    public DeliveryZoneResponse update(
            Long id,
            SaveDeliveryZoneRequest request
    ) {
        DeliveryZone zone = findEntity(id);

        zone.update(
                request.neighborhood(),
                request.deliveryFee(),
                request.displayOrder(),
                request.active() != null
                        ? request.active()
                        : zone.getActive()
        );

        return DeliveryZoneResponse.from(zone);
    }

    private DeliveryZone findEntity(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bairro de entrega não encontrado"
                        )
                );
    }
}