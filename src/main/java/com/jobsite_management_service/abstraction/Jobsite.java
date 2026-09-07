package com.jobsite_management_service.abstraction;

import com.jobsite_management_service.abstraction.enums.JobsiteType;

//Factory will use jobsite to create
public interface Jobsite {
    User getRequestor();
    String getAddress();
    JobsiteType getJobsiteType();
}
