package com.bvt.encodezip.mapper;

import com.bvt.encodezip.entity.File;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface FileMapper {

    void receiveFile(String fileName);

    void receiveFile(String fileName, String fileSuffix, String teleporter);

    File findFileByFileName(String fileName);
}
