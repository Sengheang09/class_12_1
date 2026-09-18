package com.example.project_class111.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.project_class111.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
public class CloudinaryService {
    @Autowired
    private Cloudinary cloudinary;

    public Map upload(MultipartFile file) {

        try {
            return cloudinary.uploader()
                    .upload(
                            file.getBytes(),
                            ObjectUtils.emptyMap()
                    );
        }catch(IOException e){
            throw new BadRequestException("fail with upload file "+e.getMessage());
        }
    }

    public Map delete(String publicId) throws IOException {
        return cloudinary.uploader()
                .destroy(
                        publicId,
                        ObjectUtils.emptyMap()
                );
    }
}
