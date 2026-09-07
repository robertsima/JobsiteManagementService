package com.jobsite_management_service.abstraction;

import com.jobsite_management_service.abstraction.enums.UserType;

//Abstraction for user type classes
public interface User {
    UserType getUserType();
    String getFullName();
    String getUserId();
    String getEmail();
}
