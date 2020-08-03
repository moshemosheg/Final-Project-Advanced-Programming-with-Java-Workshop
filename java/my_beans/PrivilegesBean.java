/** ***********************************************************
 * class representing the bean of actions being done on
 * user of website by a administrator.
 * actions are: register/unregister user, and updating 
 * users "admin" privileges.
 ************************************************************* */

package my_beans;

import my_types.User;
import java.sql.Connection;
import java.sql.SQLException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

@ManagedBean(name = "privilegesBean")
public class PrivilegesBean {

    @ManagedProperty(value = "#{databaseBean}")
    private DatabaseBean database;
    private User user;

    public PrivilegesBean() {
        user = new User();
    }

    public DatabaseBean getDatabase() {
        return database;
    }

    public void setDatabase(DatabaseBean database) {
        this.database = database;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // registers user to website by adding user to table of "users" in database
    public void addUser() {
        try {
            if (this.database.getDataSource() == null) {
                throw new SQLException("Unable to obtain DataSource");
            }
            try (Connection connection = this.database.getDataSource().getConnection()) {
                if (connection == null) {
                    throw new SQLException("Unable to obtain Connection");
                }
                this.database.insertOrUpdateInTbleUSERS(connection, this.user);
            }
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage facesMessage = new FacesMessage("Success!");
            context.addMessage(null, facesMessage);
        } catch (SQLException ex) {
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage facesMessage = new FacesMessage("Error: couldn't Add User!!!");
            context.addMessage(null, facesMessage);
        }
    }

    // unregisters user from website by deleting user from table of "users" in database
    public void deleteUser() {
        try {
            if (this.database.getDataSource() == null) {
                throw new SQLException("Unable to obtain DataSource");
            }
            try (Connection connection = this.database.getDataSource().getConnection()) {
                if (connection == null) {
                    throw new SQLException("Unable to obtain Connection");
                }
                this.database.deleteUserFromUSERS_table(connection, this.user.getEmail());
            }
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage facesMessage = new FacesMessage("Success!");
            context.addMessage(null, facesMessage);
        } catch (SQLException ex) {
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage facesMessage = new FacesMessage("Error:Faild To Delete!!");
            context.addMessage(null, facesMessage);
        }
    }

    //updates users "admin" privileges
    public void editPrivileges() {
        try {
            if (this.database.getDataSource() == null) {
                throw new SQLException("Unable to obtain DataSource");
            }
            try (Connection connection = this.database.getDataSource().getConnection()) {
                if (connection == null) {
                    throw new SQLException("Unable to obtain Connection");
                }
                this.database.updateUserPrivilegeInTbleUSERS(connection, this.user);
            }
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage facesMessage = new FacesMessage("Success!");
            context.addMessage(null, facesMessage);
        } catch (SQLException ex) {
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage facesMessage = new FacesMessage("Error:Faild To Change Privilege!!");
            context.addMessage(null, facesMessage);
        }
    }

}
