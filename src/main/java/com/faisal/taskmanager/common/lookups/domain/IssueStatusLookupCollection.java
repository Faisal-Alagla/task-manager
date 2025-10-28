package com.faisal.taskmanager.common.lookups.domain;

import com.faisal.taskmanager.common.lookups.entities.IssueStatusLk;

import java.util.List;

public class IssueStatusLookupCollection extends BaseLookupCollection<IssueStatusLk> {

    public IssueStatusLookupCollection(List<IssueStatusLk> items) {
        super(items);
    }

}