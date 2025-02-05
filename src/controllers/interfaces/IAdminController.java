package controllers.interfaces;

import models.Admin;

public interface IAdminController {
    String createAdmin(Admin admin);
    Admin getAdminByUsername(String username);  // <-- ДОЛЖНО БЫТЬ ТАК
    String getAllAdmins();
    String updateAdminRole(int id, String newRole);
    String deleteAdmin(int id);
}

