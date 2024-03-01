package com.driver.controllers;


import com.driver.EntryDto.ProductionHouseEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.services.ProductionHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/production")
public class ProductionHouseController {
    @Autowired
    ProductionHouseService productionHouseService;



    @PostMapping("/add")
    public Integer addProductionHouseInfoIntoDb(@RequestBody ProductionHouseEntryDto productionHouseEntryDto){

        //Default ratings of the ProductionHouse should be 0
        return productionHouseService.addProductionHouseToDb(productionHouseEntryDto);

    }


//    public ResponseEntity<String> addProductionHouseInfoIntoDb(@RequestBody ProductionHouseEntryDto productionHouseEntryDto) {
//        Integer result = productionHouseService.addProductionHouseToDb(productionHouseEntryDto);
//        if (result != null && result > 0) {
//            return ResponseEntity.ok("Production house added successfully with ID: " + result);
//        } else {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add production house");
//        }
//    }

}
