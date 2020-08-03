/************************************************************************
 * class representing data from database that user is interested to view
 ************************************************************************/
package my_types;


public class DataToShow {

    private boolean id;
    private boolean firstName;
    private boolean lastName;
    private boolean dateOfBirth;
    private boolean postponementArmy;
    private boolean marriageStatus;
    private boolean studentsEmail;
    private boolean yearOfSchool;
    private boolean dateOfStart;
    private boolean dateOfEnd;
    private boolean street;
    private boolean city;
    private boolean country;
    private boolean zipNumber;
    private boolean houseNumber;
    private boolean studentPhone;
    private boolean fatherFirstName;
    private boolean fatherId;
    private boolean motherFirstName;
    private boolean motherId;
    private boolean housePhone;
    private boolean fatherCellphone;
    private boolean motherCellphone;
    private boolean parentEmail;  
    private String alumniOrCurrent ="";
    private String MarriedOrSingle ="";
    private String PostponementOrNot ="";

    public boolean isId() {
        return id;
    }

    public void setId(boolean id) {
        this.id = id;
    }

    public boolean isFirstName() {
        return firstName;
    }

    public void setFirstName(boolean firstName) {
        this.firstName = firstName;
    }

    public boolean isLastName() {
        return lastName;
    }

    public void setLastName(boolean lastName) {
        this.lastName = lastName;
    }

    public boolean isDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(boolean dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean isPostponementArmy() {
        return postponementArmy;
    }

    public void setPostponementArmy(boolean postponementArmy) {
        this.postponementArmy = postponementArmy;
    }

    public boolean isMarriageStatus() {
        return marriageStatus;
    }

    public void setMarriageStatus(boolean marriageStatus) {
        this.marriageStatus = marriageStatus;
    }

    public boolean isStudentsEmail() {
        return studentsEmail;
    }

    public void setStudentsEmail(boolean studentsEmail) {
        this.studentsEmail = studentsEmail;
    }

    public boolean isYearOfSchool() {
        return yearOfSchool;
    }

    public void setYearOfSchool(boolean yearOfSchool) {
        this.yearOfSchool = yearOfSchool;
    }

    public boolean isDateOfStart() {
        return dateOfStart;
    }

    public void setDateOfStart(boolean dateOfStart) {
        this.dateOfStart = dateOfStart;
    }

    public boolean isDateOfEnd() {
        return dateOfEnd;
    }

    public void setDateOfEnd(boolean dateOfEnd) {
        this.dateOfEnd = dateOfEnd;
    }

    public boolean isStreet() {
        return street;
    }

    public void setStreet(boolean street) {
        this.street = street;
    }

    public boolean isCity() {
        return city;
    }

    public void setCity(boolean city) {
        this.city = city;
    }

    public boolean isCountry() {
        return country;
    }

    public void setCountry(boolean country) {
        this.country = country;
    }

    public boolean isZipNumber() {
        return zipNumber;
    }

    public void setZipNumber(boolean zipNumber) {
        this.zipNumber = zipNumber;
    }

    public boolean isHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(boolean houseNumber) {
        this.houseNumber = houseNumber;
    }

    public boolean isStudentPhone() {
        return studentPhone;
    }

    public void setStudentPhone(boolean studentPhone) {
        this.studentPhone = studentPhone;
    }

    public boolean isFatherFirstName() {
        return fatherFirstName;
    }

    public void setFatherFirstName(boolean fatherFirstName) {
        this.fatherFirstName = fatherFirstName;
    }

    public boolean isFatherId() {
        return fatherId;
    }

    public void setFatherId(boolean fatherId) {
        this.fatherId = fatherId;
    }

    public boolean isMotherFirstName() {
        return motherFirstName;
    }

    public void setMotherFirstName(boolean motherFirstName) {
        this.motherFirstName = motherFirstName;
    }

    public boolean isMotherId() {
        return motherId;
    }

    public void setMotherId(boolean motherId) {
        this.motherId = motherId;
    }

    public boolean isHousePhone() {
        return housePhone;
    }

    public void setHousePhone(boolean housePhone) {
        this.housePhone = housePhone;
    }

    public boolean isFatherCellphone() {
        return fatherCellphone;
    }

    public void setFatherCellphone(boolean fatherCellphone) {
        this.fatherCellphone = fatherCellphone;
    }

    public boolean isMotherCellphone() {
        return motherCellphone;
    }

    public void setMotherCellphone(boolean motherCellphone) {
        this.motherCellphone = motherCellphone;
    }

    public boolean isParentEmail() {
        return parentEmail;
    }

    public void setParentEmail(boolean parentEmail) {
        this.parentEmail = parentEmail;
    }
    public String getAlumniOrCurrent() {
        return alumniOrCurrent;
    }

    public void setAlumniOrCurrent(String alumniOrCurrent) {
        this.alumniOrCurrent = alumniOrCurrent;
    }

    public String getMarriedOrSingle() {
        return MarriedOrSingle;
    }

    public void setMarriedOrSingle(String MarriedOrSingle) {
        this.MarriedOrSingle = MarriedOrSingle;
    }

    public String getPostponementOrNot() {
        return PostponementOrNot;
    }

    public void setPostponementOrNot(String PostponementOrNot) {
        this.PostponementOrNot = PostponementOrNot;
    }

}
