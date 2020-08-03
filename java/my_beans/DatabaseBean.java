/******************************************************************************
 * class representing the bean of actions being done on database.
 * actions are:inserting,updating,deleting data from database,
 * and different methods for getting information from database
 ******************************************************************************/

package my_beans;

import my_types.User;
import my_types.DataToShow;
import my_types.Address;
import my_types.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.annotation.sql.DataSourceDefinition;
import javax.faces.bean.ManagedBean;
import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

@ManagedBean(name = "databaseBean")

@DataSourceDefinition(
        name = "java:global/jdbc/Students",
        className = "org.apache.derby.jdbc.ClientDataSource",
        url = "jdbc:derby://localhost:1527/Students",
        databaseName = "Students",
        user = "moshe",
        password = "mosheg")
public class DatabaseBean {

    @Resource(lookup = "java:global/jdbc/Students")
    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertOrUpdateInTbleSTUDENT(Connection connection, Student student)
            throws SQLException {
        try {
            PreparedStatement addEntryToStudent;
            // if student is in database-updates student, else inserts student
            if (!isInTable("STUDENT", "ID", student.getId())) {
                addEntryToStudent = connection.prepareStatement("INSERT INTO STUDENT"
                        + "(FIRST_NAME,LAST_NAME,BIRTHDAY,POSTPONEMENT_FROM_ARMY, "
                        + " MARRIAGE_STATUS,EMAIL,ID) VALUES(?,?,?,?,?,?,?)");
            } else {
                addEntryToStudent = connection.prepareStatement("UPDATE STUDENT SET FIRST_NAME =? "
                        + " , LAST_NAME =? , BIRTHDAY =?,POSTPONEMENT_FROM_ARMY=? "
                        + " ,MARRIAGE_STATUS=?,EMAIL = ? WHERE ID=?");
            }
            addEntryToStudent.setString(1, student.getFirstName());
            addEntryToStudent.setString(2, student.getLastName());
            addEntryToStudent.setDate(3, java.sql.Date.valueOf(student.getBirthday()));
            addEntryToStudent.setBoolean(4, student.isPostponementArmy());
            addEntryToStudent.setString(5, student.getMarriageStatus());
            addEntryToStudent.setString(6, student.getStudentsEmail());
            addEntryToStudent.setString(7, student.getId());
            addEntryToStudent.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException("Unable to insert or update in Student table");
        }
    }

    public void insertOrUpdateInTbleADDRESS_BOOK(Connection connection, Address address,
            String id) throws SQLException {
        try {
            PreparedStatement addEntryToAddressBook;
            if (!isInTable("ADDRESS_BOOK", "ID", id)) {
                addEntryToAddressBook = connection.prepareStatement("INSERT INTO ADDRESS_BOOK"
                        + "(STREET,HOUSE_NUMBER,CITY,COUNTRY,ZIP_NUMBER,ID)"
                        + "VALUES(?,?,?,?,?,?)");
            } else {
                addEntryToAddressBook = connection.prepareStatement("UPDATE ADDRESS_BOOK SET STREET =? ,"
                        + " HOUSE_NUMBER = ?,CITY = ?,COUNTRY = ?,ZIP_NUMBER =? WHERE ID=?");
            }
            addEntryToAddressBook.setString(1, address.getStreet());
            addEntryToAddressBook.setString(2, address.getHouseNumber());
            addEntryToAddressBook.setString(3, address.getCity());
            addEntryToAddressBook.setString(4, address.getCountry());
            if (address.getZipNumber() != null) {
                addEntryToAddressBook.setString(5, address.getZipNumber());
            } else {
                addEntryToAddressBook.setNull(5, java.sql.Types.INTEGER);
            }
            addEntryToAddressBook.setString(6, id);
            addEntryToAddressBook.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException("Unable to insert or update in ADDRESS_BOOK table");
        }
    }

    public void insertOrUpdateInTbleCELLPHONE_NUMBERS(Connection connection,
            String id, String number) throws SQLException {
        try {
            PreparedStatement addEntryToPhoneBook;
            if (!isInTable("CELLPHONE_NUMBERS", "ID", id)) {
                addEntryToPhoneBook = connection.prepareStatement("INSERT INTO CELLPHONE_NUMBERS "
                        + "(NUMBER,ID)"
                        + " VALUES(?,?)");
            } else {
                addEntryToPhoneBook = connection.prepareStatement("UPDATE CELLPHONE_NUMBERS "
                        + " SET NUMBER = ? WHERE ID = ?");
            }
            addEntryToPhoneBook.setString(1, number);
            addEntryToPhoneBook.setString(2, id);
            addEntryToPhoneBook.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException("Unable to insert or update ID:" + id + " in CELLPHONE_NUMBERS table");
        }
    }

    public void insertOrUpdateInTbleFATHER_OF_STUDENT(Connection connection,
            Student student) throws SQLException {
        try {
            PreparedStatement addEntryToFotherOfStudent;
            if (!isInTable("FATHER_OF_STUDENT", "STUDENT_ID", student.getId())) {
                addEntryToFotherOfStudent = connection.prepareStatement("INSERT INTO FATHER_OF_STUDENT"
                        + "(FATHER_ID,STUDENT_ID) VALUES(?,?)");
            } else {
                addEntryToFotherOfStudent = connection.prepareStatement("UPDATE FATHER_OF_STUDENT "
                        + " SET FATHER_ID =? WHERE STUDENT_ID=?");
            }
            addEntryToFotherOfStudent.setString(1, student.getFatherId());
            addEntryToFotherOfStudent.setString(2, student.getId());
            addEntryToFotherOfStudent.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException("Unable to insert or update in FATHER_OF_STUDENT table");
        }
    }

    public void insertOrUpdateInTblePARENTS(Connection connection, Student student) throws SQLException {
        try {
            PreparedStatement addEntryToParents;
            if (!isInTable("PARENTS", "FATHER_ID", student.getFatherId())) {
                addEntryToParents = connection.prepareStatement("INSERT INTO PARENTS"
                        + "(FIRST_NAME_FATHER,FIRST_NAME_MOTHER,MOTHER_ID,HOUSE_PHONE_NUMBER,"
                        + "PARENTS_EMAIL,FATHER_ID) VALUES(?,?,?,?,?,?)");
            } else {
                addEntryToParents = connection.prepareStatement("UPDATE PARENTS "
                        + "SET FIRST_NAME_FATHER = ?,FIRST_NAME_MOTHER = ?,"
                        + "MOTHER_ID = ?,HOUSE_PHONE_NUMBER = ?,"
                        + "PARENTS_EMAIL = ? WHERE FATHER_ID =?");
            }
            addEntryToParents.setString(1, student.getFatherFirstName());
            addEntryToParents.setString(2, student.getMotherFirstName());
            addEntryToParents.setString(3, student.getMotherId());
            addEntryToParents.setString(4, student.getHousePhone());
            addEntryToParents.setString(5, student.getParentEmail());
            addEntryToParents.setString(6, student.getFatherId());
            addEntryToParents.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException("Unable to insert or update in PARENTS table");
        }
    }

    public void insertOrUpdateInTbleSTUDENTS_IN_INSTITUTION(Connection connection, Student student)
            throws SQLException {
        try {
            PreparedStatement addEntryTostudentsInIntitution;
            if (!isInTable("STUDENTS_IN_INSTITUTION", "ID", student.getId())) {
                addEntryTostudentsInIntitution = connection.prepareStatement("INSERT INTO "
                        + " STUDENTS_IN_INSTITUTION (YEAR_OF_SCHOOL,DATE_OF_END,DATE_OF_START,ID)"
                        + "VALUES(?,?,?,?)");
            } else {
                addEntryTostudentsInIntitution = connection.prepareStatement("UPDATE STUDENTS_IN_INSTITUTION "
                        + " SET YEAR_OF_SCHOOL = ?,DATE_OF_END = ?,DATE_OF_START = ? WHERE ID =?");
            }
            addEntryTostudentsInIntitution.setInt(1, student.getYearOfSchool());
            if (student.getDateEnding() != null) {
                addEntryTostudentsInIntitution.setDate(2, java.sql.Date.valueOf(student.getDateEnding()));
            } else {
                java.sql.Date date = null;
                addEntryTostudentsInIntitution.setDate(2, date);
            }
            addEntryTostudentsInIntitution.setDate(3, java.sql.Date.valueOf(student.getDateOfStart()));
            addEntryTostudentsInIntitution.setString(4, student.getId());
            addEntryTostudentsInIntitution.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException("Unable to insert or update in STUDENTS_IN_INSTITUTION table");
        }
    }

    public void insertOrUpdateInTbleUSERS(Connection connection,
            User user) throws SQLException {
        try {
            PreparedStatement addEntryToUsers;
            if (!isInTable("USERS", "EMAIL", user.getEmail())) {
                addEntryToUsers = connection.prepareStatement("INSERT INTO USERS "
                        + "(ADMIN,FIRST_NAME,LAST_NAME,EMAIL)"
                        + " VALUES(?,?,?,?)");
            } else {
                addEntryToUsers = connection.prepareStatement("UPDATE USERS "
                        + " SET  ADMIN=?,FIRST_NAME=?,LAST_NAME=?  WHERE EMAIL = ?");
            }
            addEntryToUsers.setBoolean(1, user.isAdmin());
            addEntryToUsers.setString(2, user.getFirstName());
            addEntryToUsers.setString(3, user.getLastName());
            addEntryToUsers.setString(4, user.getEmail());
            addEntryToUsers.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException("Unable to insert or update ID:" + user.getEmail()
                    + " in USERS table");
        }
    }

    // returns list of data of specific data from columns requested by user
    public ArrayList<List<String>> getSelectedColumnsFromAllTables(DataToShow dataToShow,
            String WHERE_statement) {
        ArrayList<List<String>> allSelectedColumnsData = new ArrayList<>();
        try {
            if (dataSource == null) {
                throw new SQLException("Unable to obtain DataSource");
            }
            try (Connection connection = dataSource.getConnection()) {
                if (connection == null) {
                    throw new SQLException("Unable to connect");
                }
                // gets all data in database about students
                PreparedStatement preparedStatement = connection.prepareStatement(""
                        + " SELECT A.ID ,A.DATE_OF_START,A.DATE_OF_END,A.YEAR_OF_SCHOOL, "
                        + " STUDENT.FIRST_NAME,STUDENT.LAST_NAME, STUDENT.BIRTHDAY, "
                        + " STUDENT.POSTPONEMENT_FROM_ARMY,STUDENT.MARRIAGE_STATUS, "
                        + " STUDENT.EMAIL AS STUDENT_EMAIL, B.NUMBER AS STUDENT_NUMBER, "
                        + " C.HOUSE_NUMBER,C.STREET,C.CITY,C.COUNTRY,C.ZIP_NUMBER, "
                        + " E.FATHER_ID,E.MOTHER_ID,E.FIRST_NAME_FATHER,E.FIRST_NAME_MOTHER, "
                        + " E.HOUSE_PHONE_NUMBER, E.PARENTS_EMAIL, F.NUMBER AS FATHER_NUMBER,"
                        + " G.NUMBER AS MOTHER_NUMBER"
                        + " FROM STUDENT INNER JOIN STUDENTS_IN_INSTITUTION AS A ON "
                        + " STUDENT.ID = A.ID LEFT JOIN CELLPHONE_NUMBERS AS B ON A.ID=B.ID "
                        + " INNER JOIN ADDRESS_BOOK AS C ON A.ID = C.ID "
                        + " INNER JOIN FATHER_OF_STUDENT AS D ON A.ID = D.STUDENT_ID "
                        + " INNER JOIN PARENTS AS E ON D.FATHER_ID = E.FATHER_ID "
                        + " LEFT JOIN CELLPHONE_NUMBERS AS F ON F.ID=D.FATHER_ID "
                        + " LEFT JOIN CELLPHONE_NUMBERS AS G ON G.ID=E.MOTHER_ID "
                        + WHERE_statement);
                CachedRowSet rowSet = RowSetProvider.newFactory().createCachedRowSet();
                rowSet.populate(preparedStatement.executeQuery());
                // add to list only data from columns that were requested by user
                while (rowSet.next()) {
                    ArrayList<String> row = new ArrayList<>();
                    if (dataToShow.isId()) {
                        row.add(rowSet.getString("ID"));
                    }
                    if (dataToShow.isFirstName()) {
                        row.add(rowSet.getString("FIRST_NAME"));
                    }
                    if (dataToShow.isLastName()) {
                        row.add(rowSet.getString("LAST_NAME"));
                    }
                    if (dataToShow.isDateOfBirth()) {
                        row.add(rowSet.getString("BIRTHDAY"));
                    }
                    if (dataToShow.isPostponementArmy()) {
                        row.add(rowSet.getString("POSTPONEMENT_FROM_ARMY"));
                    }
                    if (dataToShow.isMarriageStatus()) {
                        row.add(rowSet.getString("MARRIAGE_STATUS"));
                    }
                    if (dataToShow.isStudentsEmail()) {
                        row.add(rowSet.getString("STUDENT_EMAIL"));
                    }
                    if (dataToShow.isYearOfSchool()) {
                        row.add(rowSet.getString("YEAR_OF_SCHOOL"));
                    }
                    if (dataToShow.isDateOfStart()) {
                        row.add(rowSet.getString("DATE_OF_START"));
                    }
                    if (dataToShow.isDateOfEnd()) {
                        row.add(rowSet.getString("DATE_OF_END"));
                    }
                    if (dataToShow.isStreet()) {
                        row.add(rowSet.getString("STREET"));
                    }
                    if (dataToShow.isHouseNumber()) {
                        row.add(rowSet.getString("HOUSE_NUMBER"));
                    }
                    if (dataToShow.isCity()) {
                        row.add(rowSet.getString("CITY"));
                    }
                    if (dataToShow.isCountry()) {
                        row.add(rowSet.getString("COUNTRY"));
                    }
                    if (dataToShow.isZipNumber()) {
                        row.add(rowSet.getString("ZIP_NUMBER"));
                    }
                    if (dataToShow.isFatherId()) {
                        row.add(rowSet.getString("FATHER_ID"));
                    }
                    if (dataToShow.isFatherFirstName()) {
                        row.add(rowSet.getString("FIRST_NAME_FATHER"));
                    }
                    if (dataToShow.isMotherId()) {
                        row.add(rowSet.getString("MOTHER_ID"));
                    }
                    if (dataToShow.isMotherFirstName()) {
                        row.add(rowSet.getString("FIRST_NAME_MOTHER"));
                    }
                    if (dataToShow.isHousePhone()) {
                        row.add(rowSet.getString("HOUSE_PHONE_NUMBER"));
                    }
                    if (dataToShow.isParentEmail()) {
                        row.add(rowSet.getString("PARENTS_EMAIL"));
                    }
                    if (dataToShow.isStudentPhone()) {
                        row.add(rowSet.getString("STUDENT_NUMBER"));
                    }
                    if (dataToShow.isFatherCellphone()) {
                        row.add(rowSet.getString("FATHER_NUMBER"));
                    }
                    if (dataToShow.isMotherCellphone()) {
                        row.add(rowSet.getString("MOTHER_NUMBER"));
                    }
                    allSelectedColumnsData.add(row);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allSelectedColumnsData;
    }

    //returns all infermation from database about student with id nnumber of "studentId" 
    public Student getStudentById(String studentId) {
        Student studentById = new Student();
        try {
            if (dataSource == null) {
                throw new SQLException("Unable to obtain DataSource");
            }
            try (Connection connection = dataSource.getConnection()) {
                if (connection == null) {
                    throw new SQLException("Unable to connect to DataBase");
                }
                // gets information about student from "Student" table 
                PreparedStatement studentDatabaseStatement = connection.prepareStatement("SELECT * "
                        + "FROM STUDENT WHERE ID = ? ");
                studentDatabaseStatement.setString(1, studentId);
                CachedRowSet rowSet = RowSetProvider.newFactory().createCachedRowSet();
                rowSet.populate(studentDatabaseStatement.executeQuery());
                while (rowSet.next()) {
                    studentById.setId(rowSet.getString("ID"));
                    studentById.setFirstName(rowSet.getString("FIRST_NAME"));
                    studentById.setLastName(rowSet.getString("LAST_NAME"));
                    studentById.setBirthday(rowSet.getDate("BIRTHDAY").toLocalDate());
                    studentById.setPostponementArmy(rowSet.getBoolean("POSTPONEMENT_FROM_ARMY"));
                    studentById.setMarriageStatus(rowSet.getString("MARRIAGE_STATUS"));
                    studentById.setStudentsEmail(rowSet.getString("EMAIL"));
                }

                String fatherId = "";
                String motherId = "";
                // gets fathers id number from "FATHER_OF_STUDENT" table 
                PreparedStatement father_of_studentDatabaseStatement = connection.prepareStatement(
                        "SELECT * FROM FATHER_OF_STUDENT WHERE STUDENT_ID = ? ");
                father_of_studentDatabaseStatement.setString(1, studentId);
                rowSet = RowSetProvider.newFactory().createCachedRowSet();
                rowSet.populate(father_of_studentDatabaseStatement.executeQuery());
                while (rowSet.next()) {
                    fatherId = rowSet.getString("FATHER_ID");
                }

                // gets information of students parents from "PARENTS" table 
                PreparedStatement parentsDatabaseStatement = connection.prepareStatement("SELECT * "
                        + "FROM PARENTS WHERE FATHER_ID = ? ");
                parentsDatabaseStatement.setString(1, fatherId);
                rowSet = RowSetProvider.newFactory().createCachedRowSet();
                rowSet.populate(parentsDatabaseStatement.executeQuery());
                while (rowSet.next()) {
                    studentById.setFatherId(fatherId);
                    studentById.setFatherFirstName(rowSet.getString("FIRST_NAME_FATHER"));
                    studentById.setMotherFirstName(rowSet.getString("FIRST_NAME_MOTHER"));
                    motherId = rowSet.getString("MOTHER_ID");
                    studentById.setMotherId(motherId);
                    studentById.setHousePhone(rowSet.getString("HOUSE_PHONE_NUMBER"));
                    studentById.setParentEmail(rowSet.getString("PARENTS_EMAIL"));
                }

                //gets students address from "ADDRESS_BOOK" table
                PreparedStatement addressBookDatabaseStatement = connection.prepareStatement(
                        " SELECT * FROM ADDRESS_BOOK WHERE ID = ? ");
                addressBookDatabaseStatement.setString(1, studentId);
                rowSet = RowSetProvider.newFactory().createCachedRowSet();
                rowSet.populate(addressBookDatabaseStatement.executeQuery());
                Address address = new Address();
                while (rowSet.next()) {
                    address.setCity(rowSet.getString("CITY"));
                    address.setCountry(rowSet.getString("COUNTRY"));
                    address.setHouseNumber(rowSet.getString("HOUSE_NUMBER"));
                    address.setStreet(rowSet.getString("STREET"));
                    address.setZipNumber(rowSet.getString("ZIP_NUMBER"));
                    studentById.setAddress(address);
                }

                // gets data of student from "STUDENTS_IN_INSTITUTION" table
                PreparedStatement studentsInIntitutionDatabaseStatement = connection.prepareStatement(
                        "SELECT * FROM STUDENTS_IN_INSTITUTION WHERE ID = ? ");
                studentsInIntitutionDatabaseStatement.setString(1, studentId);
                rowSet = RowSetProvider.newFactory().createCachedRowSet();
                rowSet.populate(studentsInIntitutionDatabaseStatement.executeQuery());
                java.sql.Date date;
                while (rowSet.next()) {
                    studentById.setDateOfStart(rowSet.getDate("DATE_OF_START").toLocalDate());
                    studentById.setYearOfSchool(rowSet.getInt("YEAR_OF_SCHOOL"));
                    date = rowSet.getDate("DATE_OF_END");
                    if (date != null) {
                        studentById.setDateEnding(date.toLocalDate());
                    } else {
                        studentById.setDateEnding(null);
                    }
                    studentById.setAddress(address);
                }

                // gets students cellphone number from "CELLPHONE_NUMBERS" table
                PreparedStatement CELLPHONE_NUMBERS_DatabaseStatement = connection.prepareStatement(
                        " SELECT * FROM CELLPHONE_NUMBERS WHERE ID = ? ");
                CELLPHONE_NUMBERS_DatabaseStatement.setString(1, studentId);
                rowSet = RowSetProvider.newFactory().createCachedRowSet();
                rowSet.populate(CELLPHONE_NUMBERS_DatabaseStatement.executeQuery());
                if (rowSet.next()) {
                    studentById.setStudentPhone(rowSet.getString("NUMBER"));
                }
                // gets students mother cellphone number from "CELLPHONE_NUMBERS" table
                CELLPHONE_NUMBERS_DatabaseStatement.setString(1, motherId);
                rowSet = RowSetProvider.newFactory().createCachedRowSet();
                rowSet.populate(CELLPHONE_NUMBERS_DatabaseStatement.executeQuery());
                if (rowSet.next()) {
                    studentById.setMotherCellphone(rowSet.getString("NUMBER"));
                }
                // gets students fathers cellphone number from "CELLPHONE_NUMBERS" table
                CELLPHONE_NUMBERS_DatabaseStatement.setString(1, fatherId);
                rowSet = RowSetProvider.newFactory().createCachedRowSet();
                rowSet.populate(CELLPHONE_NUMBERS_DatabaseStatement.executeQuery());
                if (rowSet.next()) {
                    studentById.setFatherCellphone(rowSet.getString("NUMBER"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return studentById;
    }

    // gets all data about registers users to website from "USERS" table
    public ArrayList<User> getUSERS_table(boolean showOnlyAdmin) {
        ArrayList<User> list = new ArrayList<>();
        try {
            if (dataSource == null) {
                throw new SQLException("Unable to obtain DataSource");
            }
            try (Connection connection = dataSource.getConnection()) {
                if (connection == null) {
                    throw new SQLException("Unable to connect");
                }
                String whereClause = "";
                if (showOnlyAdmin) {
                    whereClause = " WHERE ADMIN=TRUE ";
                }
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT * FROM USERS " + whereClause);
                CachedRowSet rowSet = RowSetProvider.newFactory().createCachedRowSet();
                rowSet.populate(preparedStatement.executeQuery());
                ArrayList<String> row = new ArrayList<>();
                while (rowSet.next()) {
                    User user = new User();
                    user.setAdmin(rowSet.getBoolean("ADMIN"));
                    user.setEmail(rowSet.getString("EMAIL"));
                    user.setFirstName(rowSet.getString("FIRST_NAME"));
                    user.setLastName(rowSet.getString("LAST_NAME"));
                    list.add(user);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    // deletes all data in database of a peer of parents of student
    public void deleteStudentParents(Connection connection, String fatherId, String motherId)
            throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                " DELETE FROM ADDRESS_BOOK WHERE ID = ? ");
        preparedStatement.setString(1, fatherId);
        preparedStatement.executeUpdate();

        preparedStatement = connection.prepareStatement(
                " DELETE FROM CELLPHONE_NUMBERS WHERE ID = ?");
        preparedStatement.setString(1, fatherId);
        preparedStatement.executeUpdate();

        preparedStatement = connection.prepareStatement(
                " DELETE FROM CELLPHONE_NUMBERS WHERE ID = ? ");
        preparedStatement.setString(1, motherId);
        preparedStatement.executeUpdate();

        preparedStatement = connection.prepareStatement(
                " DELETE FROM PARENTS WHERE FATHER_ID = ? ");
        preparedStatement.setString(1, fatherId);
        preparedStatement.executeUpdate();

    }

    // deletes all student with id number of "studentId" data from database
    public void deleteStudent(Connection connection, String studentId)
            throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                " DELETE FROM ADDRESS_BOOK WHERE ID = ? ");
        preparedStatement.setString(1, studentId);
        preparedStatement.executeUpdate();
        preparedStatement = connection.prepareStatement(
                " DELETE FROM CELLPHONE_NUMBERS WHERE ID = ? ");
        preparedStatement.setString(1, studentId);
        preparedStatement.executeUpdate();
        preparedStatement = connection.prepareStatement(
                " DELETE FROM STUDENTS_IN_INSTITUTION WHERE ID = ? ");
        preparedStatement.setString(1, studentId);
        preparedStatement.executeUpdate();
        preparedStatement = connection.prepareStatement(
                " DELETE FROM STUDENT WHERE ID = ? ");
        preparedStatement.setString(1, studentId);
        preparedStatement.executeUpdate();
        preparedStatement = connection.prepareStatement(
                " DELETE FROM FATHER_OF_STUDENT WHERE STUDENT_ID = ? ");
        preparedStatement.setString(1, studentId);
        preparedStatement.executeUpdate();
    }

    public void deleteUserFromUSERS_table(Connection connection, String email) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE "
                + " FROM USERS WHERE EMAIL = ? ");
        preparedStatement.setString(1, email);
        preparedStatement.executeUpdate();
    }

    public void updateUserPrivilegeInTbleUSERS(Connection connection,
            User user) throws SQLException {
        try {
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement("UPDATE USERS "
                    + " SET  ADMIN=? WHERE EMAIL = ?");
            preparedStatement.setBoolean(1, user.isAdmin());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException("Unable to insert or update ID:" + user.getEmail()
                    + " in USERS table");
        }
    }

    // returns true if "email" is in database with admin privileges
    public boolean isAdmin(String email) {
        try {
            if (dataSource == null) {
                throw new SQLException("Unable to obtain DataSource");
            }
            try (Connection connection = dataSource.getConnection()) {
                if (connection == null) {
                    throw new SQLException("Unable to connect");
                }
                PreparedStatement user = connection.prepareStatement("SELECT ADMIN "
                        + " FROM USERS WHERE EMAIL =? ");
                user.setString(1, email);
                CachedRowSet rowSet = RowSetProvider.newFactory().createCachedRowSet();
                rowSet.populate(user.executeQuery());
                while (rowSet.next()) {
                    if (rowSet.getBoolean("ADMIN")) {
                        return true;
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    // returns true if "PrimaryKey" is in table names "table"
    public boolean isInTable(String table, String nameOfPrimaryKeyCoulmn,
            String PrimaryKey) throws SQLException {
        try (Connection connec = dataSource.getConnection()) {
            if (connec == null) {
                throw new SQLException("Unable to obtain Connection");
            }
            PreparedStatement checkIfExist = connec.prepareStatement("SELECT * FROM " + table
                    + " WHERE " + nameOfPrimaryKeyCoulmn + " = ?");
            checkIfExist.setString(1, PrimaryKey);
            ResultSet a = checkIfExist.executeQuery();
            if (a.next()) {
                return true;
            }
        }
        return false;
    }

    // returns true if user with email address of "email" is in database as registered
    public boolean isRegisteredUser(String email) {
        boolean isRegistered = false;
        try {
            if (dataSource == null) {
                throw new SQLException("Unable to obtain DataSource");
            }
            try (Connection connection = dataSource.getConnection()) {
                if (connection == null) {
                    throw new SQLException("Unable to connect");
                }
                PreparedStatement user = connection.prepareStatement("SELECT EMAIL "
                        + " FROM USERS WHERE EMAIL =? ");
                user.setString(1, email);
                CachedRowSet rowSet = RowSetProvider.newFactory().createCachedRowSet();
                rowSet.populate(user.executeQuery());
                if (rowSet.next()) {
                    isRegistered = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isRegistered;
    }
}
