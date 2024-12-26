package com.alibou.security.file;


import com.alibou.security.file.exception.FileUploadExceptionAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Stream;

@Service
public class FileStrorageService {

    @Autowired
    private FileDBRepository fileDBRepository;
    @Autowired
    private FileUploadExceptionAdvice fileUploadExceptionAdvice;


    public FileDB store(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileDB FileDB = new FileDB(fileName,file.getContentType(), file.getBytes());
        return fileDBRepository.save(FileDB);
    }

    public FileDB getFile(String id) {
        return fileDBRepository.findById(id).get();
    }

    public Stream<FileDB> getAllFiles() {
        return fileDBRepository.findAll().stream();
    }


}
