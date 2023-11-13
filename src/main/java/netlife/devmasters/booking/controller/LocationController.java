package netlife.devmasters.booking.controller;

import netlife.devmasters.booking.domain.Location;
import netlife.devmasters.booking.domain.TypeResource;
import netlife.devmasters.booking.domain.dto.LocationCreate;
import netlife.devmasters.booking.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/location")
public class LocationController {
    @Autowired
    private LocationService service;
    @PostMapping("/create")
    public Location createLocation(@RequestBody LocationCreate location){
        return service.save(location);
    }
    @GetMapping("/getAll")
    public Iterable<Location> getAll(){
        return service.getAll();
    }
}
