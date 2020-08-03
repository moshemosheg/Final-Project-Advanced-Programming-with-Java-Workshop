/***********************************************************************
 * custom validators for input in website.
 ***********************************************************************/

package my_filters_converters_and_validators;

import my_types.Email;
import my_beans.DatabaseBean;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

@ManagedBean(name = "myValidator")

public class MyValidator {

    private static final String GOOGLE_CLIENT_ID
            = "845616635154-9gb74oiqnsptrfai1amdadhdj281aft7.apps.googleusercontent.com";

 
    // authenticates that the token was sent now from google 
    public void validateGoogleSignInToken(FacesContext context,
            UIComponent component, Object value) throws ValidatorException {
        try {
            String tokenString = (String) value;
            JacksonFactory jacksonFactory = new JacksonFactory();
            GoogleIdTokenVerifier googleIdTokenVerifier = new GoogleIdTokenVerifier(
                    new NetHttpTransport(), jacksonFactory);
            GoogleIdToken token = GoogleIdToken.parse(jacksonFactory, tokenString);

            /*
             * The GoogleIdTokenVerifier.verify() method verifies the JWT signature,
             * the aud claim, the iss claim, and the exp claim.
             */
            if (googleIdTokenVerifier.verify(token)) {
                GoogleIdToken.Payload payload = token.getPayload();
                if (!GOOGLE_CLIENT_ID.equals(payload.getAudience())) {
                    throw new ValidatorException(new FacesMessage(
                            "error verifying googles authenticator"));
                } else if (!GOOGLE_CLIENT_ID.equals(payload.getAuthorizedParty())) {
                    throw new ValidatorException(new FacesMessage(
                            "error verifying googles authenticator"));
                }
            } else {
                throw new ValidatorException(new FacesMessage("error verifying googles authenticator"));
            }
        } catch (Exception ex) {
            throw new ValidatorException(new FacesMessage("error verifying googles authenticator"));
        }
    }

    // validates that email is legal.treats empty string as legal
    public void validateEmailInput(FacesContext context,
            UIComponent component, Object value) throws ValidatorException {
        String emailInputText = (String) value;
        if (!emailInputText.equals("")) {
            if (!Email.isValidEmail(emailInputText)) {
                throw new ValidatorException(new FacesMessage("Invalid Email"));
            }
        }
    }

    /*
     * Custom validater for "templte.xhtml" page. validates that if "email"
     *  box was selected, the email address is currect
     */
    public void validateEmailInputIfEmailSelected(FacesContext context,
            UIComponent component,Object value) throws ValidatorException {
        UIInput confirmComponent = (UIInput) component.getAttributes().get("emailInputText");
        String emailInputText = (String) confirmComponent.getSubmittedValue();
        if (value.toString().equals("email")) {
            if (!Email.isValidEmail(emailInputText)) {
                throw new ValidatorException(new FacesMessage("Invalid Email"));
            }
        }
    }

    public void validateIsLegalEmail(FacesContext context,
            UIComponent component, Object value) throws ValidatorException {
        if (!Email.isValidEmail((String) value)) {
            throw new ValidatorException(new FacesMessage("Invalid Email"));
        }
    }

    // checks if email is registered in database to be able to view website
    public void validateRegisteredEmail(FacesContext context,
            UIComponent component, Object value) throws ValidatorException {
        if (!isRegistered(context, (String) value)) {
            throw new ValidatorException(new FacesMessage("Email not Registered!"));
        }
    }

    // validates that input to "inputtext" is numbers or that no input was inputed
    public void validateisEmptyOrNumber(FacesContext context,
            UIComponent component,Object value) throws ValidatorException {
        String number = (String) value;
        if (!number.equals("")) {
            try {
                Long.parseLong(number);
            } catch (NumberFormatException ex) {
                throw new ValidatorException(new FacesMessage("Enter valid number"));
            }
        }
    }

    /*
     * method validates that:1- student with same Id dosent
     * exist in student table in database. 2-validates that id input is length of
     * legal ID NUMBER - 9 digits.
     */
    public void validateIdIsNotInDatabase(FacesContext context, UIComponent component,
            Object value) throws ValidatorException {
        validateLegalId(context, component, value);
        if (idLegalAndIsInSTUDENT_table(context, (String) value)) {
            throw new ValidatorException(new FacesMessage("Student exist in database"));
        }
    }

    /*
     * method validates that:1- student with same Id 
     * is in student table. 2-validates that id input is length of
     * legal ID NUMBER - 9 digits.
     */
    public void validateIdInDatabase(FacesContext context, UIComponent component,
            Object value) throws ValidatorException {
        if (!idLegalAndIsInSTUDENT_table(context, (String) value)) {
            throw new ValidatorException(new FacesMessage("Student does not exist in database"));
        }
    }

    // validates that input is 9 digits
    public void validateLegalId(FacesContext context, UIComponent component,
            Object value) throws ValidatorException {
        if (!isLegalId((String) value)) {
            throw new ValidatorException(new FacesMessage("Enter 9 digit Id number"));
        }
    }

    public boolean idLegalAndIsInSTUDENT_table(FacesContext context, String id)
            throws ValidatorException {
        Application application = context.getApplication();
        DatabaseBean databaseBean = application.evaluateExpressionGet(context, "#{databaseBean}",
                DatabaseBean.class);
        if (databaseBean.getDataSource() == null) {
            throw new ValidatorException(new FacesMessage(
                    "Could'nt validate if Student exist in database"));
        } else {
            try (Connection connection = databaseBean.getDataSource().getConnection()) {
                if (connection == null) {
                    throw new ValidatorException(new FacesMessage("Unable to connect to DataSource"));
                } else if (isLegalId(id)) {
                    PreparedStatement checkIfExist = connection.prepareStatement(
                            "SELECT * FROM STUDENT WHERE ID = ?");
                    checkIfExist.setString(1, id);
                    ResultSet a = checkIfExist.executeQuery();
                    if (a.next()) {
                        return true;
                    }
                }
            } catch (SQLException ex) {
                throw new ValidatorException(new FacesMessage(
                        "Could'nt validate if Student exist in database"));
            }
        }
        return false;
    }

    public boolean isLegalId(String id) {
        if (id.length() == 9) {
            try {
                Integer.parseInt(id);
                return true;
            } catch (NumberFormatException ex) {
            }
        }
        return false;
    }

    public boolean isRegistered(FacesContext context, String email) throws ValidatorException {
        Application application = context.getApplication();
        DatabaseBean databaseBean = application.evaluateExpressionGet(context,
                "#{databaseBean}", DatabaseBean.class);
        if (databaseBean.getDataSource() == null) {
            throw new ValidatorException(new FacesMessage("Could'nt validate if Email is Registered"));
        } else {
            try (Connection connection = databaseBean.getDataSource().getConnection()) {
                if (connection == null) {
                    throw new ValidatorException(new FacesMessage("Unable to obtain DataSource"));
                } else {
                    PreparedStatement checkIfExist = connection.prepareStatement(
                            "SELECT * FROM USERS WHERE EMAIL = ?");
                    checkIfExist.setString(1, email);
                    ResultSet a = checkIfExist.executeQuery();
                    if (a.next()) {
                        return true;
                    }
                }
            } catch (SQLException ex) {
                throw new ValidatorException(new FacesMessage("Could'nt validate if Email is Registered"));
            }
        }
        return false;
    }
}
