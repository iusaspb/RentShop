package org.rent.app.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
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
    /**
     *  As we do not use Client in this example
     *  This method
     * @return  current/default Client
     */
    public Mono<Long> getCurrent(){
        return Mono.just(1L);
    };

}
