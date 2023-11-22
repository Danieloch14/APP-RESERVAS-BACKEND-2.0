package netlife.devmasters.booking.controller;

import netlife.devmasters.booking.domain.Location;
import netlife.devmasters.booking.domain.dto.LocationCreate;
import netlife.devmasters.booking.exception.domain.DataException;
import netlife.devmasters.booking.service.LocationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {
    @Autowired
    private LocationService service;
    @Autowired
    private ModelMapper modelMapper;
    @PostMapping("")
    public Location createLocation(@RequestBody LocationCreate locationDto) throws DataException {
        Location location = modelMapper.map(locationDto, Location.class);
        return service.save(location);
    }
    @GetMapping("")
    public Iterable<Location> getAll(){
        return service.getAll();
    }
    @GetMapping("/{id}")
    public Location getById(@PathVariable("id") Integer id){
        return service.getById(id).get();
    }
    @PutMapping("/{id}")
    public Location update(@PathVariable("id") Integer id, @RequestBody LocationCreate locationDto) throws DataException {
        Location location = modelMapper.map(locationDto, Location.class);
        return service.update(location,id);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) throws Exception {
        service.delete(id);
    }
}
