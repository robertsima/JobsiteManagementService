package com.jobsite_management_service.abstraction;

//abstraction layer to represent orders and quotes
public interface Request {
    Jobsite getJobsite();
    Provider getProvider();
    User getRequestor();
    String getStatus();
}
