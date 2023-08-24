package cc.cybereflex.resources.controller;

import cc.cybereflex.client.domain.person.PersonDTO;
import cc.cybereflex.common.enums.ResultEnum;
import cc.cybereflex.common.model.Result;
import cc.cybereflex.resources.model.request.PersonRequest;
import cc.cybereflex.resources.service.PersonServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/resources/manage/person")
@RequiredArgsConstructor
@RestController
public class PersonManageController {

    private final PersonServiceImpl personService;

    @PostMapping("add_person")
    public Result<Void> addPerson(@RequestBody @Validated PersonRequest request) {
        PersonDTO personDTO = new PersonDTO();
        BeanUtils.copyProperties(request, personDTO);
        boolean result = personService.insert(personDTO);
        return result ? Result.success(ResultEnum.SUCCESS) : Result.failed(ResultEnum.PERSON_CREATE_FAILED);
    }


}
