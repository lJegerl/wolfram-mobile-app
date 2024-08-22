package ru.ctraltdefeat.DemoWolframSingle.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ctraltdefeat.DemoWolframSingle.calculation.Bridge;
import ru.ctraltdefeat.DemoWolframSingle.domain.CalculationRequest;
import ru.ctraltdefeat.DemoWolframSingle.domain.CalculationResponse;

@RestController
@RequestMapping("api/calculation")
@RequiredArgsConstructor
public class CalculationController {

    final int RESULT_OK = 0;

    @GetMapping("health")
    public ResponseEntity<String> healthCheck()
    {
        return ResponseEntity.ok("Healthy");
    }

    @GetMapping
    public ResponseEntity<String> SayHello()
    {
        Bridge.SayHello();
        return ResponseEntity.ok("Called c++");
    }

    @PostMapping("calculate")
    public ResponseEntity<CalculationResponse> calculate(@RequestBody CalculationRequest calculationRequest)
    {
        CalculationResponse calculationResponse = new CalculationResponse();
        int resultCode = Bridge.calculate(calculationRequest, calculationResponse);
        if (resultCode == RESULT_OK)
        {
            return ResponseEntity.ok(calculationResponse);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
