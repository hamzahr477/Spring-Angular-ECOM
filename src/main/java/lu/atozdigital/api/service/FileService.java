package lu.atozdigital.api.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;


@Service
@Slf4j

public class FileService {
    @Value("${spring.servlet.multipart.location}")
    private String path;
    @Value("${server.port}")
    String port;
    public String uploadImage(Long id,MultipartFile image) throws IOException {
        if(Objects.requireNonNull(image.getContentType()).split("/")[0].equals("image")) {
            String dest = "\\" + id + "." + FilenameUtils.getExtension(image.getOriginalFilename());
            image.transferTo(new File(path+dest));
            log.info(new File(path+dest).getAbsolutePath());
            return "/articles/image/"+id + "." + FilenameUtils.getExtension(image.getOriginalFilename());
        }
        log.info(path);

        throw new IOException();
    }

    public byte[] getImage(String imageUrl) throws IOException {
        byte[] image = new byte[0];
        image = FileUtils.readFileToByteArray(new File(path+"\\"+imageUrl));
        return image;
    }


}
