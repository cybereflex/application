package cc.cybereflex.resources.controller;

import cc.cybereflex.common.enums.ResultEnum;
import cc.cybereflex.common.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> invalidArgumentExp(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();

        List<String> errorMessages = bindingResult.getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .toList();
        logger.error("{}",errorMessages);

        return Result.failed(ResultEnum.ARGUMENT_VALID_FAILED);

    }

}
