package netlife.devmasters.booking.controller;

import netlife.devmasters.booking.domain.TypeResource;
import netlife.devmasters.booking.exception.domain.DataException;
import netlife.devmasters.booking.service.TypeResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/type-resources")
public class TypeResourceController {
    @Autowired
    private TypeResourceService service;
    @GetMapping("")
    public Iterable<TypeResource> getAll(){
        return service.getAll();
    }

    @PostMapping("")
    public TypeResource save(@RequestBody TypeResource obj) throws DataException {
       return service.save(obj);
    }
    @PutMapping("/{id}")
    public TypeResource update(@PathVariable("id")  Integer id, @RequestBody TypeResource obj) throws DataException {
        return service.update(obj,id);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) throws Exception {
        service.delete(id);
    }

}
