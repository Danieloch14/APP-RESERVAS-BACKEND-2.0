package netlife.devmasters.booking.controller;

import netlife.devmasters.booking.domain.TypeResource;
import netlife.devmasters.booking.domain.dto.TypeResourceCreate;
import netlife.devmasters.booking.service.TypeResourceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/typeResource")
public class TypeResourceController {
    @Autowired
    private TypeResourceService service;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/getAll")
    public Iterable<TypeResource> getAll(){
        return service.getAll();
    }

    @PostMapping("/save")
    public TypeResource save(@RequestBody TypeResource obj){
       return service.save(obj);
    }
    @PutMapping("/update")
    public TypeResource update(@RequestBody TypeResource obj){
        return service.update(obj);
    }
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") int id) throws Exception {
        service.delete(id);
    }

}
