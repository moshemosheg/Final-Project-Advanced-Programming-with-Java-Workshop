/** ***********************************************************
 * class representing the bean of actions being done on student.
 * actions are: deleting from database,adding to database,and
 * editing student from database.
 ************************************************************* */
package my_beans;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import my_types.Student;

@ManagedBean(name = "studentBean")

public class StudentBean {

    @ManagedProperty(value = "#{databaseBean}")
    private DatabaseBean database;
    private Student student;
    private boolean deleteParents;

    public StudentBean() {
        this.database = new DatabaseBean();
        this.student = new Student();
    }

    public DatabaseBean getDatabase() {
        return database;
    }

    public void setDatabase(DatabaseBean database) {
        this.database = database;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public boolean isDeleteParents() {
        return deleteParents;
    }

    public void setDeleteParents(boolean deleteParents) {
        this.deleteParents = deleteParents;
    }

    /*
     * add's all data of student to their tables in database.
     * and returns message if was successful or not.
     */
    public String addStudent() {

        try {
            if (this.database.getDataSource() == null) {
                throw new SQLException("Unable to obtain DataSource");
            }
            try (Connection connection = this.database.getDataSource().getConnection()) {
                if (connection == null) {
                    throw new SQLException("Unable to obtain Connection");
                }
                //transaction processing
                connection.setAutoCommit(false);
                try {
                    // adds all data of "Student" to all tables
                    this.database.insertOrUpdateInTbleADDRESS_BOOK(connection,
                            this.student.getAddress(), this.student.getId());
                    if (this.student.isSameAddress()) {
                        //save students address also as parents address
                        this.database.insertOrUpdateInTbleADDRESS_BOOK(connection,
                                this.student.getAddress(), this.student.getFatherId());
                    }
                    this.database.insertOrUpdateInTbleSTUDENT(connection, this.student);
                    this.database.insertOrUpdateInTbleFATHER_OF_STUDENT(connection, this.student);
                    this.database.insertOrUpdateInTblePARENTS(connection, this.student);
                    this.database.insertOrUpdateInTbleCELLPHONE_NUMBERS(connection,
                            this.student.getId(), this.student.getStudentPhone());
                    this.database.insertOrUpdateInTbleCELLPHONE_NUMBERS(connection,
                            this.student.getFatherId(), this.student.getFatherCellphone());
                    this.database.insertOrUpdateInTbleCELLPHONE_NUMBERS(connection,
                            this.student.getMotherId(), this.student.getMotherCellphone());
                    this.database.insertOrUpdateInTbleSTUDENTS_IN_INSTITUTION(connection, this.student);
                    connection.commit();

                    // send message to user that student was added successful
                    FacesContext context = FacesContext.getCurrentInstance();
                    FacesMessage facesMessage = new FacesMessage("Success!");
                    context.addMessage(null, facesMessage);
                } catch (SQLException ex) {
                    connection.rollback();
                    // send message to user that student wasn't added
                    FacesContext context = FacesContext.getCurrentInstance();
                    FacesMessage facesMessage = new FacesMessage("Error: couldnt add entry!");
                    context.addMessage(null, facesMessage);
                    Logger.getLogger(DatabaseBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            // send message to user that student wasn't added
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage facesMessage = new FacesMessage("Error: couldnt add entry!");
            context.addMessage(null, facesMessage);
            Logger.getLogger(DatabaseBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public String deleteStudent() {
        try {
            if (this.database.getDataSource() == null) {
                throw new SQLException("Unable to obtain DataSource");
            }
            try (Connection connection = this.database.getDataSource().getConnection()) {
                if (connection == null) {
                    throw new SQLException("Unable to obtain Connection");
                }
                if (this.deleteParents) {
                    //gets father of students id from database
                    Student studentToDelete = this.database.getStudentById(this.student.getId());
                    // delets parents data
                    this.database.deleteStudentParents(connection,
                            studentToDelete.getFatherId(), studentToDelete.getMotherId());
                }
                // delets students data
                this.database.deleteStudent(connection, this.student.getId());
                // send user success message
                FacesContext context = FacesContext.getCurrentInstance();
                FacesMessage facesMessage = new FacesMessage("Success!");
                context.addMessage(null, facesMessage);
            }
        } catch (SQLException ex) {
            // send user error message
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage facesMessage = new FacesMessage("Error: Couldnt delete Student!!!");
            context.addMessage(null, facesMessage);
        }
        return "";
    }

    //populates input boxes of "editStudentPage2" with data of student from database
    public String setStudentAndRedirect() {
        this.student = this.database.getStudentById(this.student.getId());
        return "editStudentPage2";
    }
}
