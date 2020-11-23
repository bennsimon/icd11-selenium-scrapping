package Restapi.Controller;

import Restapi.Service.CollectionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    @Async
    @GetMapping("/icd")
    public ResponseEntity<ModelMap> index() {
        try {
            collectionService.startCollection();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ModelMap modelMap = new ModelMap();
        modelMap.put("message", "collection started");
        return ResponseEntity.ok(modelMap);
    }

    @GetMapping
    public ResponseEntity<ModelMap> show(@RequestParam(required = false) String name,
                                         @RequestParam(required = false) String code) {
        if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(code)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            ModelMap modelMap = new ModelMap();
            modelMap.put("data", collectionService.findAllByNameOrCode(name, code));
            return ResponseEntity.ok(modelMap);
        }
    }
}
