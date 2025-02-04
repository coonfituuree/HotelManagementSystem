package repositories.interfaces;

import models.Customer;
import java.util.List;

public interface ICustomerRepository {
    boolean createCustomer(Customer customer);
    Customer getCustomerById(int id);
    List<Customer> getAllCustomers();

    // Добавляем метод поиска клиента по email
    Customer getCustomerByEmail(String email);
}
