package io.futurebound.manageit;



public class DataModel {
    String _code;
    String _product;
    String _quantity;
    String cost;
    String _date;

    public DataModel(){

    }
    public DataModel(String code, String product, String quantity, String _date , String cost){
        this._code =code;
        this._product =product;
        this.cost=cost;
        this._quantity =quantity;
        this._date =_date;
    }
    public String getCode() {
        return _code;
    }

    public String getProduct() {
        return _product;
    }

    public String getQuantity() {
        return _quantity;
    }
    public  String getCost(){return  cost;}

    public String getDat() {
        return _date;
    }

}
