package nova.hackathon.util.cloudStorage;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private final Storage storage;

    private static final String BUCKET_NAME = "uhackathon";

    public List<String> uploadImages(List<MultipartFile> files, Long boardId) {
        List<String> uploadedUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                String filename = generateFileName(Objects.requireNonNull(file.getOriginalFilename()));
                String objectName = String.format("user/%d/%s", boardId, filename);

                BlobInfo blobInfo = BlobInfo.newBuilder(BUCKET_NAME, objectName)
                        .setContentType(file.getContentType())
                        .build();

                storage.create(blobInfo, file.getBytes());
                Acl acl = Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER);
                storage.createAcl(blobInfo.getBlobId(), acl);

                String publicUrl = String.format("https://storage.googleapis.com/%s/%s", BUCKET_NAME, objectName);
                uploadedUrls.add(publicUrl);
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드 실패: " + file.getOriginalFilename(), e);
            }
        }

        return uploadedUrls;
    }

    private String generateFileName(String originalName) {
        String extension = originalName.substring(originalName.lastIndexOf("."));
        return UUID.randomUUID() + extension;
    }
}
