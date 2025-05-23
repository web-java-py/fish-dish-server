package com.server.controller.agriculture;

import io.swagger.annotations.Api;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log
@RestController
@RequestMapping("/agriculture/class")
@Api(tags = "种类")
public class AgricultureClassController {
}
