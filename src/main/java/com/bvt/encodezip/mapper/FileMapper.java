package com.bvt.encodezip.mapper;

import com.bvt.encodezip.entity.File;
import com.bvt.encodezip.vo.FileVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FileMapper {

    void receiveFile(String fileName);

    int receiveFileComplete(String fileName, String fileSuffix, String filePath, String aliasName, String teleporter);

    List<FileVO> getAllFile();

    File findFileByFileName(String fileName);
    FileVO findFileByFileAliasName(String fileName);

}
