package netlife.devmasters.booking.controller;

import netlife.devmasters.booking.domain.Region;
import netlife.devmasters.booking.exception.domain.DataException;
import netlife.devmasters.booking.service.RegionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/regions")
public class RegionController {
    @Autowired
    private RegionService service;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("")
    public Iterable<Region> getAll(){
        return service.getAll();
    }
    @PostMapping("")
    public Region save(@RequestBody Region obj) throws DataException {
       return service.save(obj);
    }
    @PutMapping("/{id}")
    public Region update(@PathVariable("id") Integer id, @RequestBody Region obj) throws DataException {
        return service.update(obj,id);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) throws Exception {
        service.delete(id);
    }
}
