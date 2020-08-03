/** ***********************************************************
 * class representing the bean of changeing template page.
 * creats the new document with students data, and saves to
 * users computer or sends to users email - depending on 
 * users choice.
 ************************************************************* */
package my_beans;

import java.io.IOException;
import my_types.MyFile;
import my_types.Student;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.mail.MessagingException;

@ManagedBean(name = "templateBean")
public class TemplateBean {

    @ManagedProperty(value = "#{databaseBean}")
    private DatabaseBean database;
    private String id;
    private String templateName;
    private String action;
    private String email;

    public TemplateBean() {
        action = "save";
    }

    public DatabaseBean getDatabase() {
        return database;
    }

    public void setDatabase(DatabaseBean database) {
        this.database = database;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String name) {
        this.templateName = name;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    // creats new file and saves it or emails it - depending on users choice
    public void result() {
        //gets students info from database
        Student student = this.database.getStudentById(id);
        //creats a new docx file with students info
        MyFile myFile = new MyFile(this.templateName + ".docx");
        String newFileName = "Student Id:" + this.id + ".docx";
        myFile.replaceTextInDocxFile(student, newFileName);
        
        //gets "CurrentInstance" for sending user message about if action was successful
        FacesContext context = FacesContext.getCurrentInstance();
        
        if (action.equals("save")) {
            try {
                myFile.download("msword");
            } catch (IOException ex) {
                FacesMessage facesMessage = new FacesMessage("Error: failed to download!");
                context.addMessage(null, facesMessage);
            }
        } else if (action.equals("email")) {
            try {
                myFile.sendEmail(this.email);
                FacesMessage facesMessage = new FacesMessage("Success!");
                context.addMessage(null, facesMessage);
            } catch (MessagingException ex) {
                FacesMessage facesMessage = new FacesMessage("Error: failed to send Email!");
                context.addMessage(null, facesMessage);
            }
        }
    }
}
