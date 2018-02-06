package daw.spring.model;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Home {

    @id
    private long id;
    private long postCode;
    private String address;

    //
    @OneToMany()
    private List<Product> productList;
    //

    public Home() {
    }

    public Home(long id, long postCode, String address, List<Product> productList) {
        this.id = id;
        this.postCode = postCode;
        this.address = address;
        this.productList = productList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPostCode() {
        return postCode;
    }

    public void setPostCode(long postCode) {
        this.postCode = postCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}

