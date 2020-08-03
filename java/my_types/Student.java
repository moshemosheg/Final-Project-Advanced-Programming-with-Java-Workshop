package my_types;

import java.time.LocalDate;

public class Student {
    private String id;
    private String firstName;
    private String lastName;
    private Integer yearOfSchool;
    private LocalDate dateOfStart;
    private LocalDate dateEnding;
    private Address address;
    private boolean sameAddress;
    private String studentPhone;
    private LocalDate birthday;
    private String studentsEmail;
    private String marriageStatus;
    private boolean postponementArmy;
    private String fatherFirstName;
    private String fatherId;
    private String motherFirstName;
    private String motherId;
    private String housePhone;
    private String fatherCellphone;
    private String motherCellphone;
    private String parentEmail;

    public Student() {
        this.address = new Address();
        this.sameAddress = true;
    }

    public Student(Student student) {
        this.id = student.id;
        this.firstName = student.firstName;
        this.lastName = student.lastName;
        this.yearOfSchool = student.yearOfSchool;
        this.dateOfStart = student.dateOfStart;
        this.dateEnding = student.dateEnding;
        this.address= student.getAddress();
        this.studentPhone = student.studentPhone;
        this.birthday = student.birthday;
        this.studentsEmail = student.studentsEmail;
        this.marriageStatus = student.marriageStatus;
        this.postponementArmy = student.postponementArmy;
        this.fatherFirstName = student.fatherFirstName;
        this.fatherId = student.fatherId;
        this.motherFirstName = student.motherFirstName;
        this.motherId = student.motherId;
        this.housePhone = student.housePhone;
        this.fatherCellphone = student.fatherCellphone;
        this.motherCellphone = student.motherCellphone;
        this.parentEmail = student.parentEmail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getYearOfSchool() {
        return yearOfSchool;
    }

    public void setYearOfSchool(Integer yearOfSchool) {
        this.yearOfSchool = yearOfSchool;
    }

    public LocalDate getDateOfStart() {
        return dateOfStart;
    }

    public void setDateOfStart(LocalDate dateOfStart) {
        this.dateOfStart = dateOfStart;
    }

    public LocalDate getDateEnding() {
        return dateEnding;
    }

    public void setDateEnding(LocalDate dateEnding) {
        this.dateEnding = dateEnding;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getStudentPhone() {
        return studentPhone;
    }

    public void setStudentPhone(String studentPhone) {
        this.studentPhone = studentPhone;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getStudentsEmail() {
        return studentsEmail;
    }

    public void setStudentsEmail(String studentsEmail) {
        this.studentsEmail = studentsEmail;
    }

    public String getMarriageStatus() {
        return marriageStatus;
    }

    public void setMarriageStatus(String marriageStatus) {
        this.marriageStatus = marriageStatus;
    }

    public boolean isPostponementArmy() {
        return postponementArmy;
    }

    public void setPostponementArmy(boolean postponementArmy) {
        this.postponementArmy = postponementArmy;
    }

    public String getFatherFirstName() {
        return fatherFirstName;
    }

    public void setFatherFirstName(String fatherFirstName) {
        this.fatherFirstName = fatherFirstName;
    }

    public boolean isSameAddress() {
        return sameAddress;
    }

    public void setSameAddress(boolean sameAddress) {
        this.sameAddress = sameAddress;
    }

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }

    public String getMotherFirstName() {
        return motherFirstName;
    }

    public void setMotherFirstName(String motherFirstName) {
        this.motherFirstName = motherFirstName;
    }

    public String getMotherId() {
        return motherId;
    }

    public void setMotherId(String motherId) {
        this.motherId = motherId;
    }

    public String getHousePhone() {
        return housePhone;
    }

    public void setHousePhone(String housePhone) {
        this.housePhone = housePhone;
    }

    public String getFatherCellphone() {
        return fatherCellphone;
    }

    public void setFatherCellphone(String fatherCellphone) {
        this.fatherCellphone = fatherCellphone;
    }

    public String getMotherCellphone() {
        return motherCellphone;
    }

    public void setMotherCellphone(String motherCellphone) {
        this.motherCellphone = motherCellphone;
    }

    public String getParentEmail() {
        return parentEmail;
    }

    public void setParentEmail(String parentEmail) {
        this.parentEmail = parentEmail;
    }

}
