public class Store {
    int storeNum;
    String address;
    String city;
    String state;
    String zipcode;
    String phone;

    public Store(int storeNum, String address, String city, String state, String zipcode, String phone){
        this.storeNum = storeNum;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.phone = phone;
    }

    public String getAddress(){
        return address;
    }

    public String getCity(){
        return city;
    }

    public String getState(){
        return state;
    }

    public String getZipcode(){
        return zipcode;
    }

    public String getPhone(){
        return phone;
    }

    public String getFullAddress(){
        return address + "\n" + city + ", " + state + " " + zipcode;
    }

    public String getContact(){
        return "Contact: " + phone;
    }
}
