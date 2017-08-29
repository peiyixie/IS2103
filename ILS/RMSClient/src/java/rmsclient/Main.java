/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmsclient;

import ejb.RMSBeanRemote;
import javax.ejb.EJB;
import java.util.Calendar;
import java.util.Scanner;

/**
 *
 * @author Peiyi
 */
public class Main {

    @EJB
    private static RMSBeanRemote rMSBean;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("=========== Welcome to RMS User Portal ============");
            System.out.print("Enter your user name: ");
            String userName = sc.nextLine();
            System.out.print("Enter the request content: ");
            String content = sc.nextLine();
            Calendar cal = Calendar.getInstance();
            long time = cal.getTimeInMillis();
            try {
                boolean result = rMSBean.createMessage(userName, content, time);
                if (result) {
                    System.out.println("Result is sent!");
                } else {
                    System.out.println("Result is not sent!");
                }
            } catch (Exception e) {
                System.err.println("Error in sending request: " + e);
            }
        }
    }

}
