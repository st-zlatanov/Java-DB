package softuni.exam.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entities.Ticket;

import java.util.Set;


@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Ticket findBySerialNumber(String serialNumber);
    Set<Ticket> getAllByPassengerId(Long id);
}
