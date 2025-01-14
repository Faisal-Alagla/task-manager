package com.faisal.taskmanager.activity;

import com.faisal.taskmanager.utils.Interfaces.BaseResponseDtoInterface;
import com.faisal.taskmanager.utils.RegexUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
class ActivityAspect {

    private final HttpServletRequest request;
    private final ActivityService activityService;

    @AfterReturning(
            pointcut = """
                    (
                         execution(@org.springframework.web.bind.annotation.RequestMapping * com.faisal.taskmanager..*.*(..)) ||
                         execution(@(@org.springframework.web.bind.annotation.RequestMapping *) * com.faisal.taskmanager..*.*(..))
                     ) && !(
                         @annotation(com.faisal.taskmanager.utils.annotations.NoActivityLogging) ||
                         @target(com.faisal.taskmanager.utils.annotations.NoActivityLogging)
                     )
                    """,
            returning = "obj"
    )
    void after(Object obj) {
        String httpMethod;
        String uri;
        String operation;
        String operationId;
        String message;

        try {
            httpMethod = request.getMethod();
            uri = request.getRequestURI().trim();

            String[] uriItems = uri.split("/");

            List<String> idList = new ArrayList<>();
            List<String> itemList = Arrays.stream(uriItems).map(item -> {
                if (RegexUtils.isValidUUID(item)) {
                    idList.add(item);
                    return "ID";
                }
                return item.toUpperCase();
            }).toList();

            operation = httpMethod + String.join("_", itemList);
            operationId = getOperationId(httpMethod, obj, idList);

            String messageTemplate = "%s data for %s, with ID %s";
            message = String.format(
                    messageTemplate,
                    getMessageAction(httpMethod),
                    getMassageSubject(uriItems),
                    operationId
            );

        } catch (Exception ex) {
            log.error("Error Happened in Extracting Log Data from Response");
            log.error(ex.getMessage());
            return;
        }

        try {
            activityService.logActivity(
                    new ActivityRequestDto(
                            httpMethod,
                            uri,
                            operation,
                            operationId,
                            message,
                            null //TODO: to be added when user services is done
                    )
            );
        } catch (Exception ex) {
            log.error("Error Happened in Inserting Log Data into DB");
            log.error(ex.getMessage());
        }
    }

    private String getOperationId(
            String httpMethod,
            Object obj,
            List<String> idList
    ) {
        if (httpMethod.equals("POST")) {
            ResponseEntity<?> response = (ResponseEntity<?>) obj;
            BaseResponseDtoInterface body = (BaseResponseDtoInterface) response.getBody();

            assert body != null;
            return String.valueOf(body.getId());

        } else {
            return idList.isEmpty() ? null : idList.get(idList.size() - 1);
        }
    }

    private String getMassageSubject(String[] uriItems) {
        if (RegexUtils.isValidUUID(uriItems[uriItems.length - 1]))
            return uriItems[uriItems.length - 2];
        else
            return uriItems[uriItems.length - 1];
    }

    private String getMessageAction(String ops) {
        return switch (ops) {
            case "GET" -> "retrieved";
            case "UPDATE" -> "updated";
            case "PATCH" -> "patched";
            case "POST" -> "created";
            case "DELETE" -> "deleted";
            default -> "did some action on";
        };
    }
}
