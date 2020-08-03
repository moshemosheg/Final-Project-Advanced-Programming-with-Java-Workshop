/** ***********************************************************
 * class representing the bean of user viewing the web app.
 * user is identified by his Gmail adrress, and authenticated
 * by a Token sent from "Google Identity Platform".
 * class methods: logging in and out the user, and
 * redirecting user to log in page.
 * login method stores user email and if is "admin" than stores
 * also "admin" in session. log out method destroys session
 ************************************************************* */
package my_beans;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.Application;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean(name = "userBean")
@ApplicationScoped
public class UserBean {

    private String email;
    private String idToken;
    private boolean admin = false;

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /* stores user email address in session,
     * and elso stores user is admin in session, if finds 
     * that user is registered as admin.
     * redirects user to home page of web app
     */
    public String logIn() {
        //stores users email address in session
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("user", this.email);

        //gets from Database if user is registerd as admin
        Application application = context.getApplication();
        DatabaseBean databaseBean = application.evaluateExpressionGet(context,
                "#{databaseBean}", DatabaseBean.class);
        this.admin = databaseBean.isAdmin(this.email);
        // if user is admin- stores true in session
        if (this.admin) {
            context = FacesContext.getCurrentInstance();
            context.getExternalContext().getSessionMap().put("admin", "true");
        }
        return "/faces/user/homePage?faces-redirect=true";
    }

    public String logOut() {
        //delete session
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        this.email = null;
        this.idToken = null;
        this.admin = false;
        return "/logIn.xhtml?faces-redirect=true";
    }

    public void redirect() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(
                    "faces/logIn.xhtml?faces-redirect=true");
        } catch (IOException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
