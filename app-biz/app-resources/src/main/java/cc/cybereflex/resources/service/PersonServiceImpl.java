package cc.cybereflex.resources.service;

import cc.cybereflex.client.domain.person.PersonDTO;
import cc.cybereflex.client.service.PersonService;
import cc.cybereflex.common.model.UserTypeEnum;
import cc.cybereflex.common.utils.JwtUtil;
import cc.cybereflex.resources.model.data.Person;
import cc.cybereflex.resources.model.request.PersonLoginRequest;
import cc.cybereflex.resources.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("client/person")
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    @Override
    @GetMapping("queryAllAvailable")
    public Optional<List<PersonDTO>> queryAllAvailable() {

        List<Person> persons = personRepository.queryAllAvailable();

        if (CollectionUtils.isNotEmpty(persons)) {
            return Optional.of(
                    persons.stream().map(person -> {
                        PersonDTO personDTO = new PersonDTO();
                        BeanUtils.copyProperties(person, personDTO);
                        return personDTO;
                    }).toList()
            );
        }

        return Optional.empty();
    }

    public boolean insert(PersonDTO dto) {
        try {
            String password = dto.getPassword();

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            Person person = Person.builder()
                    .uid(UUID.randomUUID().toString())
                    .username(dto.getUsername())
                    .password(passwordEncoder.encode(password))
                    .idCard(dto.getIdCard())
                    .age(dto.getAge())
                    .name(dto.getName())
                    .build();

            personRepository.insert(person);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Optional<String> login(PersonLoginRequest request) {
        Person person = personRepository.queryByUsername(request.getUsername());
        if (Objects.isNull(person)) {
            return Optional.empty();
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassword = person.getPassword();
        String password = request.getPassword();

        if (passwordEncoder.matches(password, encodePassword)) {
            String token = JwtUtil.generateToken(
                    person.getUid(), person.getUsername(),
                    null, UserTypeEnum.PERSON,
                    Collections.emptyMap()
            );
            return Optional.of(token);
        }

        return Optional.empty();
    }

    public boolean isUnusedUsername(String username) {
        Person person = personRepository.queryByUsername(username);
        return Objects.isNull(person);
    }


}
