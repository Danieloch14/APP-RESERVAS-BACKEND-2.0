package netlife.devmasters.booking.controller;

import netlife.devmasters.booking.domain.Resource;
import netlife.devmasters.booking.domain.dto.ResourceCreate;
import netlife.devmasters.booking.service.ResourceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resource")
public class ResourceController {

    @Autowired
    private ResourceService service;
    @Autowired
    private ModelMapper modelMapper;
    @GetMapping("/getAll")
    public Iterable<Resource> getAll(){
        return service.getAll();
    }
    @PostMapping("/save")
    public Resource save(@RequestBody ResourceCreate obj){
        Resource resource = modelMapper.map(obj, Resource.class);
       return service.save(resource);
    }
    @PostMapping("/update")
    public Resource update(@RequestBody Resource obj){
        return service.update(obj);
    }
    @PostMapping("/delete/{id}")
    public void delete(@PathVariable("id") int id) throws Exception {
        service.delete(id);
    }

}
