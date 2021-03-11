package Project.domain.facebook;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FacebookPicture {
    private PictureData data;

    public PictureData getData() {
        return data;
    }

    public void setData(PictureData data) {
        this.data = data;
    }
}