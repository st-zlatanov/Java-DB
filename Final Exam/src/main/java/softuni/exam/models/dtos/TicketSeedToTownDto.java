package softuni.exam.models.dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="to-town")
@XmlAccessorType(XmlAccessType.FIELD)
public class TicketSeedToTownDto {
    @XmlElement(name="name")
    private String toTown;

    public TicketSeedToTownDto() {
    }

    public String getToTown() {
        return toTown;
    }

    public void setToTown(String toTown) {
        this.toTown = toTown;
    }
}
