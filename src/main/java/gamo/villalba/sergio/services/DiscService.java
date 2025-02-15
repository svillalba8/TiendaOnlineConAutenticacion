package gamo.villalba.sergio.services;

import gamo.villalba.sergio.models.DiscModel;
import gamo.villalba.sergio.repositories.DiscRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class DiscService {

    @Autowired
    private DiscRepository discRepository;

    public ArrayList<DiscModel> getDiscs() {
        return (ArrayList<DiscModel>) discRepository.findAll();
    }

    public Optional<DiscModel> getDiscById(Long id) {
        return discRepository.findById(id);
    }

    public ArrayList<DiscModel> findDiscs(String title, Integer tracks, String author, Integer year) {
        if (tracks == null)  tracks = 0;
        if (year == null) year = 0;

        Integer finalYear = year;
        Integer finalTracks = tracks;

        return new ArrayList<>(getDiscs().stream()
                .filter(disc ->
                        (title == null || title.isEmpty() || disc.getTitle().equalsIgnoreCase(title)) &&
                        (author == null || author.isEmpty() || disc.getAuthor().equalsIgnoreCase(author)) &&
                        (finalTracks == 0 ||  finalTracks.equals(disc.getTracks())) &&
                        (finalYear == 0 || finalYear.equals(disc.getYear()))
                ).toList());
    }

    public DiscModel addDisc(DiscModel discModel) {
        return discRepository.save(discModel);
    }

    public DiscModel updateDisc(DiscModel discModel) {
        return discRepository.save(discModel);
    }

    public boolean deleteDiscById(Long id) {
        try {
            discRepository.deleteById(id);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
