/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hisense
 */
public class JavaTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        IGniteTest igntest = new IGniteTest();
        try
        {
            //igntest.JdbcTest();
            igntest.cacheTest();
        }catch(Exception exp)
        {
            System.out.println(exp.getMessage());
        }
    }
    
}
