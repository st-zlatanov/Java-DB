package softuni.exam.models.dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="from-town")
@XmlAccessorType(XmlAccessType.FIELD)
public class TicketSeedFromTownDto {
    @XmlElement(name="name")
    private String fromTown;

    public TicketSeedFromTownDto() {
    }

    public String getFromTown() {
        return fromTown;
    }

    public void setFromTown(String fromTown) {
        this.fromTown = fromTown;
    }
}
