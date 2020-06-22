package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import softuni.exam.models.dtos.PassengerSeedDto;
import softuni.exam.models.entities.Passenger;
import softuni.exam.models.entities.Ticket;
import softuni.exam.models.entities.Town;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.repository.TicketRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.util.ValidationUtil;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static softuni.exam.common.GlobalConstants.*;


@Service
public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepository passengerRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;
    private final TownRepository townRepository;
    private final TicketRepository ticketRepository;

    public PassengerServiceImpl(PassengerRepository passengerRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson, TownRepository townRepository, TicketRepository ticketRepository) {
        this.passengerRepository = passengerRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
        this.townRepository = townRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public boolean areImported() {
        return this.passengerRepository.count()>0;
    }

    @Override
    public String readPassengersFileContent() throws IOException {
        return Files.readString(Path.of(PASSENGERS_FILE_PATH));
    }

    @Override
    public String importPassengers() throws FileNotFoundException {
        StringBuilder resultInfo =  new StringBuilder();

        PassengerSeedDto[] dtos = this.gson.fromJson
                (new FileReader(PASSENGERS_FILE_PATH), PassengerSeedDto[].class);


        Arrays.stream(dtos).forEach(passengerSeedDto -> {
            if(this.validationUtil.isValid(passengerSeedDto)){
                if(this.passengerRepository
                        .findByEmail(passengerSeedDto.getEmail())
                        ==null){

                    Passenger passenger = this.modelMapper
                            .map(passengerSeedDto, Passenger.class);

                    Town town = this.townRepository.findByName(passengerSeedDto.getTown());
                    passenger.setTown(town);


                    resultInfo.append(String.format("Successfully imported Passenger %s %s", passengerSeedDto.getLastName(),
                            passengerSeedDto.getEmail()));
                    this.passengerRepository.saveAndFlush(passenger);


                }else{
                    resultInfo.append(ALREADY_IN_DB);
                }
            }else{
                resultInfo.append(INCORRECT_DATA_MESSAGE)
                        .append("Passenger");
            }

            resultInfo.append(System.lineSeparator());
        });

       return resultInfo.toString();
    }

    @Override
    public String getPassengersOrderByTicketsCountDescendingThenByEmail() {

        return this.passengerRepository.findAllOrderByTicketDescAndEmail().stream().map(
                p -> {
                    Set<Ticket> tickets = this.ticketRepository.getAllByPassengerId(p.getId());
                    return String.format("Passenger %s %s\n" +
                            "\tEmail - %s\n" +
                            "\tPhone - %s\n" +
                            "\tNumber of tickets - %s\n",
                            p.getFirstName(), p.getLastName(),
                            p.getEmail(),
                            p.getPhoneNumber(),
                            tickets.size()


                    );
                }
        ).collect(Collectors.joining(System.lineSeparator()));
    }
}
