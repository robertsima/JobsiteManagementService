package com.jobsite_management_service.abstraction;

import java.util.List;

// Business composite class
public interface Business {
    List<Jobsite> getJobSites();
    List<User> getUsers();
    List<Provider> getProviders();
}
