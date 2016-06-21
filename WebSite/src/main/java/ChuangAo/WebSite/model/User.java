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
    

    public User(Integer id, String name, Short group, String authority,String pass,String ownaccounts) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.authority = authority;
        this.pass = pass;
        this.ownaccounts = ownaccounts;
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
}
