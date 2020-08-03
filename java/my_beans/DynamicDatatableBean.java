/*
 * creates dynamic h:datatable code with data,to display dynamic data chosen 
 * by user to be displayed in datatable.
 * has method to download the dynamic datatable to an Excrl file.
 */
package my_beans;

import my_types.DataToShow;
import my_types.MyFile;
import static com.sun.faces.el.ELUtils.createValueExpression;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlColumn;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;

@ManagedBean(name = "dynamicDatatableBean")
@SessionScoped
public class DynamicDatatableBean implements Serializable{

    private DataToShow dataToShow;
    private ArrayList<List<String>> dynamicList;
    private ArrayList<String> dynamicHeaders;
    private HtmlPanelGroup dynamicDataTable;

    public DynamicDatatableBean() {
        dataToShow = new DataToShow();
        dynamicList = new ArrayList<>();
        dynamicHeaders = new ArrayList<>();
    }

    public DataToShow getDataToShow() {
        return dataToShow;
    }

    public void setDataToShow(DataToShow dataToShow) {
        this.dataToShow = dataToShow;
    }

    public List<List<String>> getDynamicList() {
        return dynamicList;
    }

    public void setDynamicList(ArrayList<List<String>> dynamicList) {
        this.dynamicList = dynamicList;
    }

    public ArrayList<String> getDynamicHeaders() {
        return dynamicHeaders;
    }

    public void setDynamicHeaders(ArrayList<String> dynamicHeaders) {
        this.dynamicHeaders = dynamicHeaders;
    }

    public void setDynamicDataTable(HtmlPanelGroup dynamicDataTable) {
        this.dynamicDataTable = dynamicDataTable;
    }

    // Dynamicly creats dataTable 
    public HtmlPanelGroup getDynamicDataTable() {
        //gets data of datatable
        setListAndHeader();
        createXhtmlSyntaxOfDataTableWithData();
        return dynamicDataTable;
    }

    // gets headers and data of datatable
    public void setListAndHeader() {
        //sets 'WHERE' clause of SQL statement
        String sqlWHERE_clause = setSQL_WHERE_clause();
        //gets all data recuired by user, from database
        FacesContext context = FacesContext.getCurrentInstance();
        Application application = context.getApplication();
        DatabaseBean databaseBean = application.evaluateExpressionGet(context,
                "#{databaseBean}", DatabaseBean.class);
        this.dynamicList = databaseBean.getSelectedColumnsFromAllTables(this.dataToShow, sqlWHERE_clause);
        //sets header of datatable dynamicly.
        setHeaderOfDataTable();
    }
   
    // creates dynamic XHTML code for showing data of "dynamicHeaders" and "dynamicList"
    private void createXhtmlSyntaxOfDataTableWithData() {
        // Create <h:dataTable value="#{dynamicDatatableBean.dynamicList}" var="dynamicItem">.
        HtmlDataTable dynamicDataTableHtml = new HtmlDataTable();
        dynamicDataTableHtml.setValueExpression("value",
                createValueExpression("#{dynamicDatatableBean.dynamicList}", List.class));
        dynamicDataTableHtml.setVar("dynamicItem");

        // Iterate over columns.
        if (dynamicList.size() > 0) {
            for (int i = 0; i < dynamicList.get(0).size(); i++) {
                // Create <h:column>.
                HtmlColumn column = new HtmlColumn();
                dynamicDataTableHtml.getChildren().add(column);

                // Create <h:outputText value="dynamicHeaders[i]"> for <f:facet name="header"> of column.
                HtmlOutputText header = new HtmlOutputText();
                header.setValue(dynamicHeaders.get(i));
                column.setHeader(header);

                // Create <h:outputText value="#{dynamicItem[" + i + "]}"> for the body of column.
                HtmlOutputText output = new HtmlOutputText();
                output.setValueExpression("value",
                        createValueExpression("#{dynamicItem.get(" + i + ")}", String.class));
                column.getChildren().add(output);
            }
        }
        // Add the datatable to <h:panelGroup binding="#{dynamicDatatableBean.dynamicDataTable}">.
        this.dynamicDataTable = new HtmlPanelGroup();
        this.dynamicDataTable.getChildren().add(dynamicDataTableHtml);
    }

    //sets the 'where' part of the sql statement
    public String setSQL_WHERE_clause() {
        String sqlWHERE_statment;
        boolean AlumniOrCurrentEqulsAll = this.dataToShow.getAlumniOrCurrent().equals("all");
        boolean MarriedOrSingleEqulsAll = this.dataToShow.getMarriedOrSingle().equals("all");
        boolean PostponementOrNotEqulsAll = this.dataToShow.getPostponementOrNot().equals("all");
        //if user wants all data then sql statement wont have 'where',else sets 'where' clause
        if (AlumniOrCurrentEqulsAll && MarriedOrSingleEqulsAll && PostponementOrNotEqulsAll) {
            sqlWHERE_statment = "";
        } else {
            sqlWHERE_statment = " WHERE ";
            //if wants data only about current students/alumni 
            if (!AlumniOrCurrentEqulsAll) {
                //null in 'DATE_OF_END' coulmn in table represents a current student  
                if (this.dataToShow.getAlumniOrCurrent().equals("alumni")) {
                    sqlWHERE_statment += " DATE_OF_END IS NOT NULL ";
                } else {
                    sqlWHERE_statment += " DATE_OF_END IS NULL ";
                }
            }
            //if wants data only about married/single students
            if (!MarriedOrSingleEqulsAll) {
                //adds 'and' to statement if its not the first in statement 
                if (!AlumniOrCurrentEqulsAll) {
                    sqlWHERE_statment += " AND ";
                }
                sqlWHERE_statment += " MARRIAGE_STATUS = '"
                        + this.dataToShow.getMarriedOrSingle() + "' ";
            }
            if (!PostponementOrNotEqulsAll) {
                if (!(AlumniOrCurrentEqulsAll && MarriedOrSingleEqulsAll)) {
                    sqlWHERE_statment += " AND ";
                }
                sqlWHERE_statment += " POSTPONEMENT_FROM_ARMY = '"
                        + this.dataToShow.getPostponementOrNot() + "' ";
            }
        }
        return sqlWHERE_statment;
    }

    //sets headers for datatable
    public void setHeaderOfDataTable() {
        dynamicHeaders.clear();
        if (this.dataToShow.isId()) {
            dynamicHeaders.add("ID");
        }
        if (this.dataToShow.isFirstName()) {
            dynamicHeaders.add("First Name");
        }
        if (this.dataToShow.isLastName()) {
            dynamicHeaders.add("Last Name");
        }
        if (this.dataToShow.isDateOfBirth()) {
            dynamicHeaders.add("Date Of Birth");
        }
        if (this.dataToShow.isPostponementArmy()) {
            dynamicHeaders.add("Postponement Army");
        }
        if (this.dataToShow.isMarriageStatus()) {
            dynamicHeaders.add("Marriage Status");
        }
        if (this.dataToShow.isStudentsEmail()) {
            dynamicHeaders.add("Students Email");
        }
        if (this.dataToShow.isYearOfSchool()) {
            dynamicHeaders.add("Year Of School");
        }
        if (this.dataToShow.isDateOfStart()) {
            dynamicHeaders.add("Date Of Start");
        }
        if (this.dataToShow.isDateOfEnd()) {
            dynamicHeaders.add("Date Of End");
        }
        if (this.dataToShow.isStreet()) {
            dynamicHeaders.add("Street");
        }
        if (this.dataToShow.isHouseNumber()) {
            dynamicHeaders.add("House Number");
        }
        if (this.dataToShow.isCity()) {
            dynamicHeaders.add("City");
        }
        if (this.dataToShow.isCountry()) {
            dynamicHeaders.add("Country");
        }
        if (this.dataToShow.isZipNumber()) {
            dynamicHeaders.add("Zip Number");
        }
        if (this.dataToShow.isFatherId()) {
            dynamicHeaders.add("Father Id");
        }
        if (this.dataToShow.isFatherFirstName()) {
            dynamicHeaders.add("Father First Name");
        }
        if (this.dataToShow.isMotherId()) {
            dynamicHeaders.add("Mother Id");
        }
        if (this.dataToShow.isMotherFirstName()) {
            dynamicHeaders.add("Mother First Name");
        }
        if (this.dataToShow.isHousePhone()) {
            dynamicHeaders.add("House Phone");
        }
        if (this.dataToShow.isParentEmail()) {
            dynamicHeaders.add("Parent Email");
        }
        if (this.dataToShow.isStudentPhone()) {
            dynamicHeaders.add("Student Phone");
        }
        if (this.dataToShow.isFatherCellphone()) {
            dynamicHeaders.add("Father Cellphone");
        }
        if (this.dataToShow.isMotherCellphone()) {
            dynamicHeaders.add("Mother Cellphone");
        }
    }

        public void downloadDatatableAsExcel() {
        // creats Excel file from "dynamicHeaders" and "dynamicList"
        MyFile file = new MyFile("Students Data from website.xlsx");
        ArrayList<List<String>> listOfData = this.dynamicList;
        listOfData.add(0, dynamicHeaders);
        file.createExcelFile(listOfData, "students Data");
        //downloads file
        try {
            file.download("vnd.ms-excel");
        } catch (IOException ex) {
            Logger.getLogger(DynamicDatatableBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
