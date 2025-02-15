package gamo.villalba.sergio.controllers;

import gamo.villalba.sergio.models.DiscModel;
import gamo.villalba.sergio.services.DiscService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController()
@RequestMapping("/discstore")
public class DiscController {

    @Autowired
    private DiscService discService;

    @GetMapping
    public ArrayList<DiscModel> getDiscs() {
        return discService.getDiscs();
    }

    @GetMapping("/{id}")
    public Optional<DiscModel> getDiscById(@PathVariable long id) {
        return discService.getDiscById(id);
    }

    @PostMapping
    public DiscModel createDisc(@RequestBody DiscModel disc) {
        if (discService.getDiscs().contains(disc)) return null;
        else return discService.addDisc(disc);
    }

    @DeleteMapping("/{id}")
    public String deleteDiscById(@PathVariable long id) {
        boolean eliminar = discService.deleteDiscById(id);
        if (eliminar) return "Disco eliminado";
        else return "No se ha podido eliminar el disco";
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiscModel> updateDiscById(
            @PathVariable long id,
            @RequestBody DiscModel discDetails) {

        Optional<DiscModel> discModel = discService.getDiscById(id);

        if (discModel.isEmpty()) return ResponseEntity.notFound().build();

        DiscModel existingDisc = discModel.get();
        existingDisc.setTitle(discDetails.getTitle());
        existingDisc.setTracks(discDetails.getTracks());
        existingDisc.setPrice(discDetails.getPrice());
        existingDisc.setAuthor(discDetails.getAuthor());
        existingDisc.setYear(discDetails.getYear());

        DiscModel updatedDisc = discService.updateDisc(existingDisc);
        return ResponseEntity.ok(updatedDisc);
    }
}
