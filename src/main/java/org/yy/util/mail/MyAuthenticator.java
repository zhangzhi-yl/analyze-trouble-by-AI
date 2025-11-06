package org.yy.util.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * 说明：发送邮件需要使用的基本信息 
 * 作者：YuanYe Q356703572
 * 
 */
public class MyAuthenticator extends Authenticator{   
    String userName=null;   
    String password=null;   
        
    public MyAuthenticator(){   
    }   
    public MyAuthenticator(String username, String password) {    
        this.userName = username;    
        this.password = password;    
    }    
    protected PasswordAuthentication getPasswordAuthentication(){   
        return new PasswordAuthentication(userName, password);   
    }   
}   
