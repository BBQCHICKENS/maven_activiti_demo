package com.lbj.service.impl;

import com.lbj.service.EmpService;
import org.activiti.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by gqq on 2017/12/27.
 */
@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    RepositoryService repositoryService;

    public void getRepositoryService() {
        System.out.println(repositoryService);
    }
}
