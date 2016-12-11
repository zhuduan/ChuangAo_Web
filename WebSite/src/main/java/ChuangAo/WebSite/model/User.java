package ChuangAo.WebSite.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name = "user")
public class User {

    @Column(name = "id", nullable = false)
    @Id
    private Integer id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "group", nullable = false)
    private Short group;
    
    @Column(name = "authority", nullable = false)
    private String authority;
    
    @Column(name = "pass", nullable = false)
    private String pass;
    
    @Column(name = "ownaccounts", nullable = false)
    private String ownaccounts;
    
    @Column(name = "email", nullable = false)
    private String email;
    
    @Column(name = "telephone", nullable = false)
    private String telephone;
    
    @Column(name = "noticetype", nullable = false)
    private Short noticetype;

    public User(Integer id, String name, Short group, 
    		String authority,String pass,String ownaccounts,
    		String email,String telephone,Short noticetype) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.authority = authority;
        this.pass = pass;
        this.ownaccounts = ownaccounts;
        this.email = email;
        this.telephone = telephone;
        this.noticetype = noticetype;
    }

    public User() {

    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public Short getGroup() {
        return group;
    }
    
    public String getAuthority() {
        return authority;
    }
    
    public String getPassword() {
        return pass;
    }
    
    public String getOwnAccounts(){
    	return ownaccounts;
    }
    
    public String getEmail(){
    	return email;
    }
    
    public String getTelephone(){
    	return telephone;
    }
    
    public Short getNoticeType(){
    	return noticetype;
    }
    
    public void setPassword(String pass){
    	this.pass = pass;
    }

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getOwnaccounts() {
		return ownaccounts;
	}

	public void setOwnaccounts(String ownaccounts) {
		this.ownaccounts = ownaccounts;
	}

	public Short getNoticetype() {
		return noticetype;
	}

	public void setNoticetype(Short noticetype) {
		this.noticetype = noticetype;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setGroup(Short group) {
		this.group = group;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}    
}
