package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.common.GlobalConstants;
import softuni.exam.models.dtos.TownSeedDto;
import softuni.exam.models.entities.Town;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static softuni.exam.common.GlobalConstants.*;

@Service
public class TownServiceImpl implements TownService {
    private final TownRepository townRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    public TownServiceImpl(TownRepository townRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.townRepository = townRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return this.townRepository.count()>0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return Files.readString(Path.of(TOWNS_FILE_PATH));
    }

    @Override
    public String importTowns() throws FileNotFoundException {
        StringBuilder resultInfo =  new StringBuilder();


        TownSeedDto[] dtos = this.gson.fromJson
                (new FileReader(TOWNS_FILE_PATH), TownSeedDto[].class);


        Arrays.stream(dtos).forEach(townSeedDto -> {
            if(this.validationUtil.isValid(townSeedDto)){
                if(this.townRepository.findByName(townSeedDto.getName())
                        ==null){

                    Town town = this.modelMapper
                            .map(townSeedDto, Town.class);


                    resultInfo.append(String.format("Successfully imported town %s %d", townSeedDto.getName(), townSeedDto.getPopulation()));
                    this.townRepository.saveAndFlush(town);


                }else{
                    resultInfo.append(ALREADY_IN_DB);
                }
            }else{
                resultInfo.append(INCORRECT_DATA_MESSAGE)
                        .append("Town");
            }

            resultInfo.append(System.lineSeparator());
        });

        return resultInfo.toString();
    }
}
