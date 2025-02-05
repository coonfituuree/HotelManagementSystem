package controllers;

import controllers.interfaces.IAdminController;
import models.Admin;
import repositories.interfaces.IAdminRepository;

import java.util.List;

public class AdminController implements IAdminController {
    private final IAdminRepository repository;

    public AdminController(IAdminRepository repository) {
        this.repository = repository;
    }

    @Override
    public String createAdmin(Admin admin) {
        boolean created = repository.createAdmin(admin);
        return created ? "Admin successfully created!" : "Failed to create admin. Username might be taken.";
    }

    @Override
    public Admin getAdminByUsername(String username) {
        return repository.getAdminByUsername(username);
    }

    // ✅ Новый метод для поиска администратора по email
    public Admin getAdminByEmail(String email) {
        return repository.getAdminByEmail(email);
    }

    @Override
    public String getAllAdmins() {
        List<Admin> admins = repository.getAllAdmins();
        return admins.isEmpty() ? "No admins found." : admins.toString();
    }

    @Override
    public String updateAdminRole(int id, String newRole) {
        boolean updated = repository.updateAdminRole(id, newRole);
        return updated ? "Admin role updated successfully!" : "Failed to update admin role.";
    }

    @Override
    public String deleteAdmin(int id) {
        boolean deleted = repository.deleteAdmin(id);
        return deleted ? "Admin deleted successfully!" : "Failed to delete admin.";
    }
}
