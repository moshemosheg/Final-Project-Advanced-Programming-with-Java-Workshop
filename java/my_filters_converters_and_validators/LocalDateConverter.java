/***************************************************************************
 * custom converter to convert between a String and a LocalDate
 */

package my_filters_converters_and_validators;


import static java.lang.Integer.parseInt;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 * Faces converter for LocalDate
 */
@FacesConverter(value = "localDateConverter")
public class LocalDateConverter implements javax.faces.convert.Converter {

    /*
     * converts the given string value into an Object of type LocalDate.
     * if String isn't of format "dd/mm/yyyy" - returns exception
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            if (value.equals("")) {
                java.sql.Date date = null;
                return date;
            } else {
                return LocalDate.of(parseInt(value.substring(6,10)),
                        parseInt(value.substring(3,5)),parseInt(value.substring(0,2)));
            }
        } catch (Exception ex) {
            throw new ConverterException(new FacesMessage("Invalid date: input format-dd/mm/yyyy"));
        }
    }

    //converts the given object of type localDate into a String
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        LocalDate dateValue = (LocalDate) value;
            return dateValue.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
