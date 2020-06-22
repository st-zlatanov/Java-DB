package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.PlaneSeedRootDto;
import softuni.exam.models.entities.Plane;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.service.PlaneService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static softuni.exam.common.GlobalConstants.*;
import static softuni.exam.common.GlobalConstants.INCORRECT_DATA_MESSAGE;


@Service
public class PlaneServiceImpl implements PlaneService {
    private final PlaneRepository planeRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    public PlaneServiceImpl(PlaneRepository planeRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.planeRepository = planeRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return this.planeRepository.count()>0;
    }

    @Override
    public String readPlanesFileContent() throws IOException {
        return Files.readString(Path.of(PLANES_FILE_PATH));
    }

    @Override
    public String importPlanes() throws JAXBException, FileNotFoundException {
        StringBuilder resultInfo = new StringBuilder();

        PlaneSeedRootDto planeSeedRootDto = this.xmlParser
                .convertFromFile(PLANES_FILE_PATH, PlaneSeedRootDto.class);

        planeSeedRootDto.getPlanes()
                .forEach(planeSeedDto -> {
                    if(this.validationUtil.isValid(planeSeedDto)){
                        if(this.planeRepository.findByRegisterNumber(planeSeedDto.getRegisterNumber())==null){
                            Plane plane = this.modelMapper.map(
                                    planeSeedDto, Plane.class
                            );

                            System.out.println();
                            this.planeRepository.saveAndFlush(plane);

                            resultInfo.append(String.format("Successfully imported Plane %s.",
                                    planeSeedDto.getRegisterNumber()));
                        }else{
                            resultInfo.append(ALREADY_IN_DB);
                        }
                    }else{
                        resultInfo.append(INCORRECT_DATA_MESSAGE).append("Plane");
                    }
                    resultInfo.append(System.lineSeparator());
                });

        return resultInfo.toString();
    }
}
