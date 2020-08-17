/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dichvucong;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author kienn
 */
public class Person {
    private ImageIcon image;
    private String id;
    private String name;
    private String gender;
    private String address;
    private String birth;
    private String issue;
    private String exp;

    public Person(ImageIcon image, String id, String name, String gender, String address, String birth, String issue, String exp) {
        this.image = image;
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.address = address;
        this.birth = birth;
        this.issue = issue;
        this.exp = exp;
    }

    public Person() {
    }

    public ImageIcon getImage() {
        return image;
    }

    public void setImage(ImageIcon image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }
    
}
