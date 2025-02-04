package controllers.interfaces;

import models.Customer;

public interface ICustomerController {
    String createCustomer(Customer customer);
    String getCustomer(int id);
    String getAllCustomers();
}

