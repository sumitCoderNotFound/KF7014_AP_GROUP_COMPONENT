package com.waterservices.monitoring.controller;

import com.waterservices.monitoring.dto.AveragesDTO;
import com.waterservices.monitoring.dto.StatusDTO;
import com.waterservices.monitoring.security.TokenValidationResponse;
import com.waterservices.monitoring.security.TokenValidator;
import com.waterservices.monitoring.service.FrontendDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/monitoring/data")
public class FrontendDataController {

    private final FrontendDataService frontendDataService;

    @Autowired
    private TokenValidator tokenValidator;

    @Autowired
    public FrontendDataController(FrontendDataService frontendDataService) {
        this.frontendDataService = frontendDataService;
    }

    private ResponseEntity<?> authenticateRequest(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);

        TokenValidationResponse validation = tokenValidator.validateToken(token);

        if (!validation.isValid()) {
            if ("expired".equals(validation.getReason())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access token expired");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token");
            }
        }

        // Token is valid
        return null;
    }

    // Latest flag status
    @GetMapping("/status/latest")
    public ResponseEntity<?> getLatestStatus(@RequestHeader("Authorization") String authHeader) {
        ResponseEntity<?> authCheck = authenticateRequest(authHeader);
        if (authCheck != null) return authCheck;

        StatusDTO status = frontendDataService.fetchLatestStatus();
        return ResponseEntity.ok(status);
    }

    // Monthly averages for all parameters
    @GetMapping("/averages/monthly")
    public ResponseEntity<?> getMonthlyAverages(@RequestParam("month") String month,
                                                @RequestHeader("Authorization") String authHeader) {
        ResponseEntity<?> authCheck = authenticateRequest(authHeader);
        if (authCheck != null) return authCheck;

        AveragesDTO averages = frontendDataService.fetchMonthlyAverages(month);
        return ResponseEntity.ok(averages);
    }

    // Monthly average for a specific parameter
    @GetMapping("/averages/monthly/{parameter}")
    public ResponseEntity<?> getMonthlyAverageForParameter(@PathVariable("parameter") String parameter,
                                                                @RequestParam("month") String month,
                                                           @RequestHeader("Authorization") String authHeader) {
        ResponseEntity<?> authCheck = authenticateRequest(authHeader);
        if (authCheck != null) return authCheck;

        Double avg = frontendDataService.fetchMonthlyAverageForParameter(parameter, month);
        return avg != null ? ResponseEntity.ok(avg) : ResponseEntity.notFound().build();
    }

    // Overall averages for all parameters
    @GetMapping("/averages/overall")
    public ResponseEntity<?> getOverallAverages(@RequestHeader("Authorization") String authHeader) {
        ResponseEntity<?> authCheck = authenticateRequest(authHeader);
        if (authCheck != null) return authCheck;

        AveragesDTO averages = frontendDataService.fetchOverallAverages();
        return ResponseEntity.ok(averages);
    }

    // Overall average for a specific parameter
    @GetMapping("/averages/overall/{parameter}")
    public ResponseEntity<?> getOverallAverageForParameter(@PathVariable("parameter") String parameter,
                                                           @RequestHeader("Authorization") String authHeader) {
        ResponseEntity<?> authCheck = authenticateRequest(authHeader);
        if (authCheck != null) return authCheck;

        Double avg = frontendDataService.fetchOverallAverageForParameter(parameter);
        return avg != null ? ResponseEntity.ok(avg) : ResponseEntity.notFound().build();
    }
}