package cc.cybereflex.media.onvif.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnvifDeviceProfile {

    /**
     * profile name
     */
    private String name;
    /**
     * profile token
     */
    private String token;

    /**
     * audio source config
     */
    private AudioSourceConfiguration audioSourceConfiguration;

    /**
     * video source config
     */
    private VideoSourceConfiguration videoSourceConfiguration;

    /**
     * video encoder config
     */
    private VideoEncoderConfiguration videoEncoderConfiguration;

    /**
     * audio encoder config
     */
    private AudioEncoderConfiguration audioEncoderConfiguration;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AudioSourceConfiguration {
        private String name;
        private String useCount;
        private String sourceToken;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class VideoSourceConfiguration {
        private String name;
        private String useCount;
        private String sourceToken;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AudioEncoderConfiguration {
        private String name;
        /**
         * token
         */
        private String token;
        /**
         * 编码协议
         */
        private String encoding;
        /**
         * 采样率
         */
        private int sampleRate;
        /**
         * 比特率
         */
        private int bitrate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class VideoEncoderConfiguration {
        private String name;
        /**
         * token
         */
        private String token;
        /**
         * 编码格式
         */
        private String encoding;
        /**
         * 分辨率-宽
         */
        private int width;
        /**
         * 分辨率-高
         */
        private int height;
        /**
         * 帧率
         */
        private int frameRate;
    }
}
