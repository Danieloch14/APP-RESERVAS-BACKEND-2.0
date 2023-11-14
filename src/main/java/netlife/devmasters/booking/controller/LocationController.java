package netlife.devmasters.booking.controller;

import netlife.devmasters.booking.domain.Location;
import netlife.devmasters.booking.domain.TypeResource;
import netlife.devmasters.booking.domain.dto.LocationCreate;
import netlife.devmasters.booking.service.LocationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/location")
public class LocationController {
    @Autowired
    private LocationService service;
    @Autowired
    private ModelMapper modelMapper;
    @PostMapping("/save")
    public Location createLocation(@RequestBody LocationCreate locationDto){
        Location location = modelMapper.map(locationDto, Location.class);
        return service.save(location);
    }
    @GetMapping("/getAll")
    public Iterable<Location> getAll(){
        return service.getAll();
    }
    @GetMapping("/getById/{id}")
    public Location getById(@PathVariable("id") Integer id){
        return service.getById(id).get();
    }
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") Integer id) throws Exception {
        service.delete(id);
    }
}
