package com.dimash.spring.restApp.controller;

import com.dimash.spring.restApp.dto.PersonDTO;
import com.dimash.spring.restApp.models.Person;
import com.dimash.spring.restApp.service.PersonService;
import com.dimash.spring.restApp.util.PersonErrorResponse;
import com.dimash.spring.restApp.util.PersonNotCreatedEx;
import com.dimash.spring.restApp.util.PersonNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController // так как все будут возвращать данные
@RequestMapping("/people")
public class PeopleController {
    private final PersonService personService;
    private final ModelMapper modelMapper;

    @Autowired
    public PeopleController(PersonService personService, ModelMapper modelMapper, ModelMapper modelMapper1) {
        this.personService = personService;
        this.modelMapper = modelMapper1;
    }

    @GetMapping("/getPeople")
    public List<PersonDTO> getPeople() {
        return personService.findAll().stream().map(this::convertToPersonDTO).toList();
    }

    @GetMapping("/getOnePerson/{id}")
    // Когда вы объявляете переменную метода
    // с аннотацией @PathVariable, Spring будет автоматически извлекать значение
    // из соответствующей части URI и привязывать его к этой переменной.
    // Значение переменной может быть
    // использовано внутри метода для выполнения нужной логики или передано
    // в другие части приложения.
    public PersonDTO getOnePerson(@PathVariable("id") int id) {
        // В этом примере, аннотация @PathVariable("id") указывает,
        // что значение переменной userId должно быть извлечено из URI-шаблона.
        // {id} в URI указывает на переменную часть URI, которая будет связана с параметром userId.
        // Если, например, запрос будет выполнен по пути "/users/123", значение userId будет равно 123.
        return convertToPersonDTO(personService.findOne(id)); // JACKSON конвертирует так как это RESTController
        // status 200 mean everything good
    }

    @ExceptionHandler // словит все ошибки которые вызываются в контроллере, пробрасываются
    // в параметре можно указать именно какое исключение должен ловить метод
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e) {
        PersonErrorResponse response = new PersonErrorResponse("Person wasn't found",
                System.currentTimeMillis());
        // HTTP ответе тело ответа (response, и статус в заголовке)
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // 404 status
    }

    // изменим тут Person на DTO
    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult) {
        // не имеет смысла какой объект в возвращаемом значении так и так сконвертирует в JSON объект
        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError error : fieldErrors) {
                stringBuilder.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append(";");
            }
            throw new PersonNotCreatedEx(stringBuilder.toString());
        }
        personService.save(convert(personDTO));
        // HTTP ответ с пустым телом и статусом 200 что все ок
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @ExceptionHandler
    // словит все ошибки которые вызываются в контроллере, пробрасываются
    // в параметре можно указать именно какое исключение должен ловить метод
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotCreatedEx e) {
        PersonErrorResponse response = new PersonErrorResponse(e.getMessage(),
                System.currentTimeMillis());
        // HTTP ответе тело ответа (response, и статус в заголовке)
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // значит что-то не так
    }

    private Person convert(PersonDTO personDTO) {
        // сам найдет все схожие поля и смапит их
        return modelMapper.map(personDTO,Person.class);
    }
    private PersonDTO convertToPersonDTO(Person person) {
        // смапит из кого в кого
        return modelMapper.map(person, PersonDTO.class);
    }
}
