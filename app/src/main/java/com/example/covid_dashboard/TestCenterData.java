package com.example.covid_dashboard;

public class TestCenterData
{
    private String StateName;
    private String Area;
    private String Center;
    TestCenterData(String State,String area,String center)
    {
        StateName=State;
        Area=area;
        Center=center;
    }
    String getText1()
    {
        return StateName;
    }
    String getText2()
    {
        return Area;
    }
    String getText3()
    {
        return Center;
    }
}
