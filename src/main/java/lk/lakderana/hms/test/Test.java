package lk.lakderana.hms.test;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    public static void main(String args[]) {

        /*String password1 = "Java2blog@";

        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$";
        boolean validPassword = isValidPassword(password1,regex);
        System.out.println("Java2blog@ is valid password:" +validPassword);

        String password2 = "helloword#123";

        String regex1 = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$";
        boolean validPassword1 = isValidPassword(password2,regex1);
        // No upper case
        System.out.println("helloword#123 is valid password:" +validPassword1);*/

        BigDecimal payment = BigDecimal.valueOf(20000);
        BigDecimal dueAmount = BigDecimal.valueOf(1);

        if(dueAmount.compareTo(BigDecimal.ZERO) == 0)
            System.out.println("adooo");

        /*if(payment.compareTo(dueAmount) > 0)
            System.out.println("exceed");
        else
            System.out.println("ok");*/

    }

    public static boolean isValidPassword(String password,String regex)
    {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
