package TurnQuest.view.commons;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilMethods {

    public UtilMethods() {
        super();
    }

    /**
     * Check if an email address is valid
     *
     * @param emailAddress The email address to be validated.     *
     * @return boolean Whether its valid or not
     */
    public static boolean isValidEmailAddress(String emailAddress) {
        // a null string is invalid
        if (emailAddress == null) {
            return false;
        }

        // a string without a "@" is an invalid email address
        else if (emailAddress.indexOf("@") < 0) {
            return false;
        }

        // a string without a "."  is an invalid email address
        else if (emailAddress.indexOf(".") < 0) {
            return false;
        }

        else if (lastEmailFieldTwoCharsOrMore(emailAddress) == false) {
            return false;
        }

        else {
            return true;
        }
    }

    /**
     * Returns true if the last email field (i.e., the country code, or something
     * like .com, .biz, .cc, etc.) is two chars or more in length, which it really
     * must be to be legal.
     */
    private static boolean lastEmailFieldTwoCharsOrMore(String emailAddress) {
        if (emailAddress == null)
            return false;
        StringTokenizer st = new StringTokenizer(emailAddress, ".");
        String lastToken = null;
        while (st.hasMoreTokens()) {
            lastToken = st.nextToken();
        }

        if (lastToken.length() >= 2) {
            return true;
        } else {
            return false;
        }
    }

    /** isEmailValid: Validate email address using Java reg ex.
     * This method checks if the input string is a valid email address.
     * @param email String. Email address to validate
     * @return boolean: true if email address is valid, false otherwise.
     */

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        /*
  Email format: A valid email address will have following format:
           [\\w\\.-]+: Begins with word characters, (may include periods and hypens).
       @: It must have a '@' symbol after initial characters.
       ([\\w\\-]+\\.)+: '@' must follow by more alphanumeric characters (may include hypens.).
   This part must also have a "." to separate domain and subdomain names.
       [A-Z]{2,4}$ : Must end with two to four alaphabets.
   (This will allow domain names with 2, 3 and 4 characters e.g pa, com, net, wxyz)

   Examples: Following email addresses will pass validation
   abc@xyz.net; ab.c@tx.gov
   */

        //Initialize reg ex for email.
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;
        //Make the comparison case-insensitive.
        Pattern pattern =
            Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }


}
