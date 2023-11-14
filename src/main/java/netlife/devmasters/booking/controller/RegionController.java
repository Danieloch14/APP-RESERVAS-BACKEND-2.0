package netlife.devmasters.booking.controller;

import netlife.devmasters.booking.domain.Region;
import netlife.devmasters.booking.service.RegionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/region")
public class RegionController {
    @Autowired
    private RegionService service;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/getAll")
    public Iterable<Region> getAll(){
        return service.getAll();
    }
    @PostMapping("/save")
    public Region save(@RequestBody Region obj){
       return service.save(obj);
    }
    @PutMapping("/update")
    public Region update(@RequestBody Region obj){
        return service.update(obj);
    }
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") int id) throws Exception {
        service.delete(id);
    }
}
