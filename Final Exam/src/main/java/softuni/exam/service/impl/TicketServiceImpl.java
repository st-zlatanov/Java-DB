package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.TicketSeedRootDto;
import softuni.exam.models.entities.Passenger;
import softuni.exam.models.entities.Plane;
import softuni.exam.models.entities.Ticket;
import softuni.exam.models.entities.Town;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.repository.TicketRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TicketService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static softuni.exam.common.GlobalConstants.*;
import static softuni.exam.common.GlobalConstants.INCORRECT_DATA_MESSAGE;

@Service
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;
    private final PassengerRepository passengerRepository;
    private final TownRepository townRepository;
    private final PlaneRepository planeRepository;

    public TicketServiceImpl(TicketRepository ticketRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser, PassengerRepository passengerRepository, TownRepository townRepository, PlaneRepository planeRepository) {
        this.ticketRepository = ticketRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.passengerRepository = passengerRepository;
        this.townRepository = townRepository;
        this.planeRepository = planeRepository;
    }

    @Override
    public boolean areImported() {
        return this.ticketRepository.count()>0;
    }

    @Override
    public String readTicketsFileContent() throws IOException {
        return Files.readString(Path.of(TICKETS_FILE_PATH));
    }

    @Override
    public String importTickets() throws JAXBException, FileNotFoundException {
        StringBuilder resultInfo = new StringBuilder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        TicketSeedRootDto ticketSeedRootDto = this.xmlParser
                .convertFromFile(TICKETS_FILE_PATH, TicketSeedRootDto.class);

        ticketSeedRootDto.getTickets()
                .forEach(ticketSeedDto -> {
                    if(this.validationUtil.isValid(ticketSeedDto)){
                        if(this.ticketRepository.findBySerialNumber(ticketSeedDto.getSerialNumber())==null){
                            Ticket ticket = this.modelMapper.map(
                                    ticketSeedDto, Ticket.class
                            );
                            LocalDateTime localDateTime=LocalDateTime.parse(ticketSeedDto.getTakeOff(),dtf);
                            ticket.setTakeOff(localDateTime);
                            Passenger passenger = this.passengerRepository.findByEmail(ticketSeedDto.getPassenger().getEmail());
                            ticket.setPassenger(passenger);
                            Town fromTown = this.townRepository.findByName(ticketSeedDto.getFromTown().getFromTown());
                            ticket.setFromTown(fromTown);
                            Town toTown = this.townRepository.findByName(ticketSeedDto.getToTown().getToTown());
                            ticket.setToTown(toTown);
                            Plane plane = this.planeRepository.findByRegisterNumber(ticketSeedDto.getPlane().getRegisterNumber());
                            ticket.setPlane(plane);



                            System.out.println();
                            this.ticketRepository.saveAndFlush(ticket);

                            resultInfo.append(String.format("Successfully imported Ticket %s - %s.",
                                    ticketSeedDto.getFromTown().getFromTown(), ticketSeedDto.getToTown().getToTown()));
                        }else{
                            resultInfo.append(ALREADY_IN_DB);
                        }
                    }else{
                        resultInfo.append(INCORRECT_DATA_MESSAGE).append("Ticket");
                    }
                    resultInfo.append(System.lineSeparator());
                });

        return resultInfo.toString();
    }
}
