package controllers;

import controllers.interfaces.ICustomerController;
import models.Customer;
import repositories.interfaces.ICustomerRepository;

import java.util.List;

public class CustomerController implements ICustomerController {
    private final ICustomerRepository repository;

    public CustomerController(ICustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public String createCustomer(Customer customer) {
        boolean created = repository.createCustomer(customer);
        return created ? "Customer successfully created!" : "Failed to create customer.";
    }

    @Override
    public String getCustomer(int id) {
        Customer customer = repository.getCustomerById(id);
        return customer != null ? customer.toString() : "Customer not found.";
    }

    @Override
    public String getAllCustomers() {
        List<Customer> customers = repository.getAllCustomers();
        return customers.isEmpty() ? "No customers found." : customers.toString();
    }

    // Новый метод для поиска клиента по email
    public Customer getCustomerByEmail(String email) {
        return repository.getCustomerByEmail(email);
    }
}
