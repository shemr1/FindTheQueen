import javax.swing.*;  
import java.awt.*;  
import java.awt.event.*;
import java.lang.Exception;
  
//create CreateLoginForm class to create login form  
//class extends JFrame to create a window where our component add  
//class implements ActionListener to perform an action on button click


class CreateLoginForm extends JFrame implements ActionListener  
{  
    //initialize button, panel, label, and text field  
    JButton b1;  
    JPanel newPanel;  
    JLabel userLabel, passLabel;  
    final JTextField  textField1, textField2;
    private boolean login1 = false;
    private boolean login2 = false;

    //calling constructor  
    CreateLoginForm()  
    {     
          
        //create label for username   
        userLabel = new JLabel();  
        userLabel.setText("Username");      //set label value for textField1  
          
        //create text field to get username from the user  
        textField1 = new JTextField(15);    //set length of the text  
  
        //create label for password  
        passLabel = new JLabel();  
        passLabel.setText("Password");      //set label value for textField2  
          
        //create text field to get password from the user  
        textField2 = new JPasswordField(15);    //set length for the password  
          
        //create submit button  
        b1 = new JButton("SUBMIT"); //set label to button  
          
        //create panel to put form elements  
        newPanel = new JPanel(new GridLayout(3, 1));  
        newPanel.add(userLabel);
        newPanel.add(textField1);
        newPanel.add(passLabel);
        newPanel.add(textField2); 
        newPanel.add(b1);
        //set border to panel   
        add(newPanel, BorderLayout.CENTER);  
          
        //perform action on button click   
        b1.addActionListener(this);     //add action listener to button  
        setTitle("LOGIN FORM");         //set title to the login form  
    }  
      
    //define abstract method actionPerformed() which will be called on button click   
    public void actionPerformed(ActionEvent ae)     //pass action listener as a parameter
    {  

    	int logintrial = 0;
        
        User user0 = new User("dannyboi","dre@margh_shelled");
        User user1 = new User("matty7","win&win99");
    	
    	
        String userValue = textField1.getText();        //get user entered username from the textField1  
        String passValue = textField2.getText();        //get user entered password from the textField2  
          
        try {
        	
        		if(login1 == false) {
        			if (userValue.equals(user0.getUsername()) && passValue.equals(user0.getPassword())) {  //if authentic, navigate user to a new page  
         			   login1 = true;
                     //create instance of the NewPage
                     PlayerFrame pf = new PlayerFrame(user0.getUsername());
                    pf.main(user0.getUsername());

                     //make page visible to the user  
                     pf.setVisible(true);
                     
                   }
        			else {
        				System.out.println("Invalid credential combo");
        			}
        		}else {
    				System.out.println("dannyboi is already logged in");
    			}
        		
        		if(login2 == false){  
                    if(userValue.equals(user1.getUsername()) && passValue.equals(user1.getPassword())){
                    	 login2 = true;
                        PlayerFrame pf = new PlayerFrame(user1.getUsername());
                        pf.main(user1.getUsername());

                         //make page visible to the user  
                         pf.setVisible(true);
                    }
                    else {
        				System.out.println("Invalid credential combo");;
        			}

                }  else {
    				System.out.println("matty7 is already logged in");
    			}
        	
        }
        catch(ArrayIndexOutOfBoundsException ex){
        	ex.printStackTrace();
        }
        
        
    }  
}  
  
class LoginForm  
{  
	
     
    public static void main(String arg[])  
    {  
    	
        try  
        {
            CreateLoginForm form = new CreateLoginForm();  
            form.setSize(300,100);
            form.setVisible(true);
        }  
        catch(Exception e)  
        {     
          
            JOptionPane.showMessageDialog(null, e.getMessage());  
        }  
    }  
}
class User {

    private String username;
    private String password;

    User(String username, String password ){
        this.password = password;
        this.username = username;
    }


    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }


}

