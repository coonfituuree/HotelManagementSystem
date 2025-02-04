package controllers.interfaces;

import models.Admin;

public interface IAdminController {
    String createAdmin(Admin admin);
    String getAdminByUsername(String username);
    String getAllAdmins();
    String updateAdminRole(int id, String newRole);
    String deleteAdmin(int id);
}
