package nl.polarfish.updatesql.controller;

import lombok.RequiredArgsConstructor;
import nl.polarfish.updatesql.service.UpdateSqlService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UpdateSqlController {

    private final UpdateSqlService updateSqlService;

    @RequestMapping(method = RequestMethod.POST, value = "/update-sql", produces = "application/sql")
    @ResponseStatus(HttpStatus.OK)
    public String postBatchUploads(@RequestBody String body, @RequestParam(value = "dbType") String dbType) {
        return updateSqlService.updateSql(body, dbType);
    }
}
