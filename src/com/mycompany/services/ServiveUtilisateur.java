/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.services;

import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Dialog;
import com.codename1.ui.TextField;
import com.codename1.ui.util.Resources;
import com.mycomany.utils.Statics;
import com.mycompany.gui.ProfileForm;
import com.mycompany.gui.SessionManager;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author aziz3
 */
public class ServiveUtilisateur {
    
    public static ServiveUtilisateur instance = null ;

    public static boolean resultOk = true;
    String json;
    
    private ConnectionRequest req;
    
    public static ServiveUtilisateur getInstance() {
        if(instance == null )
            instance = new ServiveUtilisateur();
        return instance ;
    }
    private Resources res;    
    public ServiveUtilisateur() {
        req = new ConnectionRequest();
        
    }
    
    public void signup(TextField password,TextField email,TextField confirmPassword ,TextField nom ,TextField prenom , Resources res ){
        
        String url = Statics.BASE_URL+"/api/Register?email="+email.getText().toString()+"&nom="+nom.getText().toString()+"&prenom="+prenom.getText().toString()+"&password="+password.getText().toString();
    
        req.setUrl(url);
        
        if(password.getText().equals(" ") && email.getText().equals(" ")) {
            
            Dialog.show("Erreur","Veuillez remplir les champs","OK",null);
            
        }
        
        req.addResponseListener((e)-> {
          
            byte[]data = (byte[]) e.getMetaData();
            String responseData = new String(data);//ba3dika na5o content 
            
            System.out.println("data ===>"+responseData);
        }
        );
        
        NetworkManager.getInstance().addToQueueAndWait(req);
    }
    
    public void signin(TextField email,TextField password, Resources rs ) {
        
        
        String url = Statics.BASE_URL+"/api/moblog?email="+email.getText().toString()+"&password="+password.getText().toString();
        req = new ConnectionRequest(url, false); //false ya3ni url mazlt matba3thtich lel server
        req.setUrl(url);
        
        req.addResponseListener((e) ->{
            
            JSONParser j = new JSONParser();
            
            String json = new String(req.getResponseData()) + "";
            
            
            try {
            
            if(json.equals("user not found")) {
                Dialog.show("Echec d'authentification","Username ou mot de passe éronné","OK",null);
            }
            else {
                System.out.println("data =="+json);
                
                Map<String,Object> user = j.parseJSON(new CharArrayReader(json.toCharArray()));
                
                
             
                //Session 
                float id = Float.parseFloat(user.get("id").toString());
                SessionManager.setId((int)id);
                
                SessionManager.setPassowrd(user.get("password").toString());
                SessionManager.setNom(user.get("nom").toString());
                SessionManager.setPrenom(user.get("prenom").toString());                
                SessionManager.setEmail(user.get("email").toString());
                
                //photo 
                
                if(user.get("photo") != null)
                    SessionManager.setImage(user.get("image").toString());
                
                
                if(user.size() >0 ) 
                   
                    System.out.println("HELLLLLOOOOOO");
                    new ProfileForm(rs).show();
                    
                    
                    }
            
            }catch(Exception ex) {
                ex.printStackTrace();
            }
            
            
            
        });
    
         //ba3d execution ta3 requete ely heya url nestanaw response ta3 server.
        NetworkManager.getInstance().addToQueueAndWait(req);
    }
    
    
    public String getPasswordByEmail(String email, Resources res ) {
        
        
        String url = Statics.BASE_URL+"/api/getPasswordByEmail?email="+email;
        req = new ConnectionRequest(url, false);
        req.setUrl(url);
        
        req.addResponseListener((e) ->{
            
            JSONParser j = new JSONParser();
            
            json = new String(req.getResponseData()) + "";
            
            
            try {
            
          
                System.out.println("data =="+json);
                
                Map<String,Object> password = j.parseJSON(new CharArrayReader(json.toCharArray()));
                
                
            
            
            }catch(Exception ex) {
                ex.printStackTrace();
            }
            
            
            
        });
    
         //ba3d execution ta3 requete ely heya url nestanaw response ta3 server.
        NetworkManager.getInstance().addToQueueAndWait(req);
    
        return json;
    }
}
