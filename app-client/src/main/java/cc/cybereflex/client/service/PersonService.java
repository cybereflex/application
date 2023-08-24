package cc.cybereflex.client.service;

import cc.cybereflex.client.domain.person.PersonDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "app-resources",path = "client/person")
public interface PersonService {

    @GetMapping("queryAllAvailable")
    Optional<List<PersonDTO>> queryAllAvailable();

}
