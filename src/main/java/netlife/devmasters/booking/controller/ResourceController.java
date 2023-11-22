package netlife.devmasters.booking.controller;

import netlife.devmasters.booking.domain.Resource;
import netlife.devmasters.booking.domain.dto.ResourceCreate;
import netlife.devmasters.booking.exception.domain.DataException;
import netlife.devmasters.booking.service.ResourceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resources")
public class ResourceController {

    @Autowired
    private ResourceService service;
    @Autowired
    private ModelMapper modelMapper;
    @GetMapping("")
    public Iterable<Resource> getAll(){
        return service.getAll();
    }
    @PostMapping("")
    public Resource save(@RequestBody ResourceCreate obj) throws DataException {
        Resource resource = modelMapper.map(obj, Resource.class);
       return service.save(resource);
    }
    @PostMapping("/{id}")
    public Resource update(@PathVariable("id") Integer id,@RequestBody Resource obj) throws DataException {
        return service.update(obj,id);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) throws Exception {
        service.delete(id);
    }

}
