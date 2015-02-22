package com.scipub.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scipub.dto.PaperSubmissionDto;

@Service
public class PaperService {

    @Transactional
    public void submitPaper(PaperSubmissionDto dto) {
        
    }
}
