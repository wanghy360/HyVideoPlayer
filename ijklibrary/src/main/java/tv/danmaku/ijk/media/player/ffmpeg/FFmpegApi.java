package tv.danmaku.ijk.media.player.ffmpeg;

public class FFmpegApi {
    @SuppressWarnings("JniMissingFunction")
    public static native String av_base64_encode(byte in[]);
}
