package org.rent.app.service;

import org.rent.app.domain.Contractor;
import org.rent.app.domain.Order;
import org.rent.app.repository.ContractorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Objects;
/**
 * ContractorService
 * <p>
 *     Contractor Service
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
@Service
public class ContractorService {
    @Autowired
    private ContractorRepository repository;

    private final static Long currentContrId = 1L;
    public Contractor getCurrent(){
        return findById(null);
    }
    public Contractor findById(Long contractorId) {
        return repository.getReferenceById(Objects.nonNull(contractorId)? contractorId:currentContrId);
    }

    public Collection<Order> getContractorOrders(@NotNull Long contractorId){
        return findById(contractorId).getOrders();
    }

}
