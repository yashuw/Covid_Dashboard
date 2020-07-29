package com.example.covid_dashboard;

public class StateData {
    private String DistrictName;
    private String active;
    private String deceased;
    private String recovered;
    private String confirmed;

    StateData(String name,String Active,String Deceased,String Recvored,String Conf)
    {
        DistrictName=name;
        active=Active;
        deceased=Deceased;
        recovered=Recvored;
        confirmed=Conf;
    }

    public String getDistrictName() {
        return DistrictName;
    }

    public String getActive() {
        return active;
    }

    public String getDeceased() {
        return deceased;
    }

    public String getRecovered() {
        return recovered;
    }

    public String getConfirmed() {
        return confirmed;
    }
}
