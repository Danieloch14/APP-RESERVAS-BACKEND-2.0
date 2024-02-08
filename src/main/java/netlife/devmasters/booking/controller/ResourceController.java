package netlife.devmasters.booking.controller;

import netlife.devmasters.booking.domain.Resource;
import netlife.devmasters.booking.domain.dto.ResourceCreate;
import netlife.devmasters.booking.domain.dto.SearchResourceDto;
import netlife.devmasters.booking.exception.domain.DataException;
import netlife.devmasters.booking.service.ResourceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/resources")
public class ResourceController {

    @Autowired
    private ResourceService service;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("")
    public Iterable<Resource> getAll() {
        return service.getAll();
    }

    @GetMapping("/by-region-name/{name}")
    public Iterable<Resource> getByRegionName(@PathVariable("name") String name) {
        return service.getByNameRegion(name);
    }

    @GetMapping("/by-region-id/{id}")
    public Iterable<Resource> getByRegionId(@PathVariable("id") int id) {
        return service.getByIdRegion(id);
    }

    @PostMapping("/availables")
    public Iterable<Resource> getByRegionId(@RequestBody SearchResourceDto searchResourceDto) throws DataException {
        return service.getAvailables(searchResourceDto);
    }

    @PostMapping("")
    public Resource save(@RequestBody Resource obj) throws DataException {
        Resource resource = modelMapper.map(obj, Resource.class);
        return service.save(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Resource> update(@PathVariable("id") Integer id, @RequestBody Resource obj) throws DataException {
        Resource resourceUpdated = service.update(obj, id);
        return ResponseEntity.ok(resourceUpdated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) throws Exception {
        service.delete(id);
    }

    @GetMapping("/{id}")
    public Optional<Resource> getById(@PathVariable("id") int id) {
        return service.getById(id);
    }
}
