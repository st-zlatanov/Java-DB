package softuni.exam.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entities.Passenger;


import java.util.List;
import java.util.Set;

@Repository
public interface PassengerRepository  extends JpaRepository<Passenger, Long> {
    Passenger findByEmail(String email);


    @Query(value = "SELECT p FROM Passenger p JOIN Ticket t ON p.id = t.passenger.id GROUP BY t.passenger ORDER BY  p.email")
    List<Passenger> findAllOrderByTicketDescAndEmail();
}
