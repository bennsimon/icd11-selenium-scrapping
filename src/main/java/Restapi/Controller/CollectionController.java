package Restapi.Controller;

import Restapi.Service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    @Async
    @GetMapping("/icd10")
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
}
