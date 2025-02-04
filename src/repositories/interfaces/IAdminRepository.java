package repositories.interfaces;

import models.Admin;
import java.util.List;

public interface IAdminRepository {
    boolean createAdmin(Admin admin);
    Admin getAdminByUsername(String username);
    List<Admin> getAllAdmins();
    boolean updateAdminRole(int id, String newRole);
    boolean deleteAdmin(int id);
}
