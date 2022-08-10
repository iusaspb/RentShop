package org.rent.app.service;

import org.rent.app.domain.Client;
import org.rent.app.domain.Order;
import org.rent.app.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Objects;
/**
 * ClientService
 * <p>
 *     Client Service
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
@Service
public class ClientService {

    private final static Long currentClientId = 1L;

    @Autowired
    private ClientRepository repository;

    public Client getCurrent(){
        return findById(null);
    }
    public Client findById(Long clientId) {
        return repository.getReferenceById(Objects.nonNull(clientId)? clientId: currentClientId);
    }

    public Collection<Order> getClientOrders(@NotNull Long clientId){
        return findById(clientId).getOrders();
    }

}
