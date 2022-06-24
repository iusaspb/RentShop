package org.rent.app.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
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
    /**
     *  As we do not use Contractor in this example
     *  This method
     * @return  current/default Contractor
     */
    public Mono<Long> getCurrent(){
        return Mono.just(1L);
    };

}
