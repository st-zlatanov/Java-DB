import entities.Employee;
import entities.Town;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Engine implements Runnable {

    private final EntityManager entityManager;
    private final BufferedReader reader;

    public Engine(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {
        //Ex 2
        //this.removeObjectsEx();

        //Ex 3
//        try {
//            this.containsEmployeeEx();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //Ex 4
        //this.employeeWithSalaryOver50000();

        //Ex 5
        this.employeesFromDepartment();
    }

    private void employeesFromDepartment() {
        List<Employee> employees = this.entityManager.createQuery(
                "select e from Employee as e " +
                        "where e.department.name = 'Research and Development'", Employee.class
        ).getResultList();
    }

    private void containsEmployeeEx() throws IOException {
        System.out.println("Enter employee full name: ");
        String fullName = this.reader.readLine();
        try{


        Employee employee = this.entityManager.createQuery(
                "select e from Employee as e "
                        + "Where concat(e.firstName, ' ', e.lastName) = :name"
                , Employee.class).setParameter("name", fullName)
                .getSingleResult();
            System.out.println("Yes");
    }catch(NoResultException nre){
            System.out.println("No");
        }
    }

    private void removeObjectsEx() {
        List<Town> towns = this.entityManager.createQuery
                ("select t from Town as t " +
                        "Where length(t.name) > 5", Town.class)
                .getResultList();
        this.entityManager.getTransaction().begin();

        towns.forEach(this.entityManager::detach);

        for (Town town : towns) {
            town.setName(town.getName().toLowerCase());
        }
        towns.forEach(this.entityManager::merge);
        this.entityManager.flush();
        this.entityManager.getTransaction().commit();


    }

    private void employeeWithSalaryOver50000(){
       this.entityManager
                .createQuery("select e from Employee as e Where e.salary > 50000", Employee.class)
                .getResultStream()
               .forEach(e-> System.out.printf("%s%n", e.getFirstName()));

        System.out.println();
    }
}
