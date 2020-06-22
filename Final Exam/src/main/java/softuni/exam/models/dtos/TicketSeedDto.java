package softuni.exam.models.dtos;

import org.hibernate.validator.constraints.Length;
import softuni.exam.models.entities.Plane;
import softuni.exam.models.entities.Town;

import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;


public class TicketSeedDto {

    private String serialNumber;

    private BigDecimal price;

    private String takeOff;

    private TicketSeedPassengerDto passenger;

    private TicketSeedPlaneDto plane;

    private TicketSeedFromTownDto fromTown;

    private TicketSeedToTownDto toTown;


    public TicketSeedDto() {
    }

    @XmlElement(name = "serial-number")
    @Length(min = 2)
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @XmlElement
    @Positive
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @XmlElement(name = "take-off")
    public String getTakeOff() {
        return takeOff;
    }

    public void setTakeOff(String takeOff) {
        this.takeOff = takeOff;
    }

    @XmlElement(name = "passenger")
    public TicketSeedPassengerDto getPassenger() {
        return passenger;
    }

    public void setPassenger(TicketSeedPassengerDto passenger) {
        this.passenger = passenger;
    }
    @XmlElement(name = "plane")
    public TicketSeedPlaneDto getPlane() {
        return plane;
    }

    public void setPlane(TicketSeedPlaneDto plane) {
        this.plane = plane;
    }

    @XmlElement(name = "from-town")
    public TicketSeedFromTownDto getFromTown() {
        return fromTown;
    }

    public void setFromTown(TicketSeedFromTownDto fromTown) {
        this.fromTown = fromTown;
    }

    @XmlElement(name = "to-town")
    public TicketSeedToTownDto getToTown() {
        return toTown;
    }

    public void setToTown(TicketSeedToTownDto toTown) {
        this.toTown = toTown;
    }
}
